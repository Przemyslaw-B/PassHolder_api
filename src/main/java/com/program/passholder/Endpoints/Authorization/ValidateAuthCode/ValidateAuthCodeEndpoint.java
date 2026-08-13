package com.program.passholder.Endpoints.Authorization.ValidateAuthCode;

import com.program.passholder.Authorization.ValidateAuthKey;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Endpoints.Authorization.Authorization2FAEndpoint.AuthKeyDTO;
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
public class ValidateAuthCodeEndpoint {
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

    @PostMapping("/codeAuthorization")
    public ResponseEntity<Map<String, Object>> codeAuthorization(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ValidateAuthCodeDTO requestBody,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (token != null && jwtUtil.validateToken(token)) {
                String userMail = jwtUtil.extractUsername(token);
                long userId = getFromMail.getUserIdFromMail(userMail);
                Optional<UserEntity> userEntity = userService.getEntityByid(userId);
                if (userEntity.isPresent()) {
                    Date currentDate = new Date();
                    Optional<Date> accountLock = userService.getLockUntil(userId);
                    if (accountLock.isPresent() && accountLock.get().after(currentDate)) {
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed", "reason", "Blocked"));
                    }

                    String userCode = requestBody.code;
                    Boolean validationStatus = false;
                    validationStatus = validateAuthKey.validate(userMail, userCode);
                    if (validationStatus) {
                        setNewLog.setLog(1, ip, userId, userId);    //Poprawna autoryzacja
                        userService.setFailedAttemps(userId, 0);
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "success"));
                    } else {
                        setNewLog.setLog(2, ip, userId, userId);    //Błędna autoryzacja
                        int failedAttemps = 1;
                        Optional<Integer> userFailedAttemps = userService.getFailedAttemps(userId);
                        if (userFailedAttemps.isPresent()) {
                            failedAttemps = userFailedAttemps.get();
                        }
                        failedAttemps += 1;
                        userService.setFailedAttemps(userId, failedAttemps);
                        if (failedAttemps >= 5) {
                            Date newLockDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);    //Data blokady Bieżąca data + 5 minut
                            userService.setLockedUntil(userId, newLockDate);
                            userService.setFailedAttemps(userId, 0);    //po blokadzie konta wyzerowac próby
                            setNewLog.setLog(30, ip, userId, userId);    //Blokada konta
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed", "reason", "Blocked"));
                        }
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed", "reason", "Niepoprawne dane."));
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed", "reason", "Niepoprawne dane."));
    }
}
