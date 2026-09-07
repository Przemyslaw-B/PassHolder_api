package com.program.passholder.Endpoints.Authorization.Authorization2FAEndpoint;

import com.program.passholder.Authentication.ValidateAuthKey;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
import com.program.passholder.Sms.SmsVerifyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthorizationEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ValidateAuthKey validateAuthKey;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    UserService userService;
    @Autowired
    SmsVerifyService smsVerifyService;

    @PostMapping("/2FA")
    public ResponseEntity<Map<String, Object>> authorization(
            @RequestBody AuthKeyDTO requestBody,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        System.out.println("Odebrany request!");

        String userEmail = requestBody.email;
        Optional<UserEntity> userEntity = userService.getEntityByMail(userEmail);
        long userId = getFromMail.getUserIdFromMail(userEmail);
        Optional<UserRoleEntity> entity = userRoleService.findByUserId(userId);
        int userRole = 0;
        Date currentDate = new Date();
        if(entity.isPresent()){
            Optional<Date> accountLock = userService.getLockUntil(userId);
            if(accountLock.isPresent() &&  accountLock.get().after(currentDate)){
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed", "reason","Blocked"));
            }
        }

        if (entity.isPresent()) {
            userRole = entity.get().getIdRole();
        }

        String authKey = requestBody.authKey;
        System.out.println("auth key + " + authKey);
        Boolean validationStatus = false;
        if (userEntity.isPresent()) {
            String userPhone = userEntity.get().getPhone();
            int authMethode = userEntity.get().getNotificationMethod();
            validationStatus = validateAuthKey.validate(userEmail, authKey);

        }
        if (validationStatus) {    //weryfikacja poprawności podanego kodu 2fa
            String email = requestBody.email;
            String token = jwtUtil.generateToken(email);
            String securityPassword = userService.getSecurityPasswordById(userId);
            String username = userEntity.get().getName();
            String salt = userEntity.get().getSalt();

            HashMap<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("token", token);
            data.put("securityPassword", securityPassword);
            data.put("status", "Validated");
            data.put("salt", salt);
            setNewLog.setLog(1, ip, userId, userId);    //Poprawna autoryzacja
            userService.setFailedAttemps(userId, 0);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "success", "role", userRole, "data", data));
        } else {
            setNewLog.setLog(2, ip, userId, userId);    //Błędna autoryzacja
            int failedAttemps = 1;
                Optional<Integer> userFailedAttemps = userService.getFailedAttemps(userId);
            if (userFailedAttemps.isPresent()) {
                failedAttemps = userFailedAttemps.get();
            }
            failedAttemps += 1;
            userService.setFailedAttemps(userId,failedAttemps);
            if(failedAttemps >= 5){
                Date newLockDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);    //Data blokady Bieżąca data + 5 minut
                userService.setLockedUntil(userId, newLockDate);
                userService.setFailedAttemps(userId, 0);    //po blokadzie konta wyzerowac próby
                setNewLog.setLog(30, ip, userId, userId);    //Blokada konta
                //return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed", "reason","Blocked"));
                return ResponseEntity.ok(Map.of("status", "OK", "auth", "failed", "reason","Blocked"));
            }
            return ResponseEntity.ok(Map.of("status", "Invalid"));
        }
    }
}
