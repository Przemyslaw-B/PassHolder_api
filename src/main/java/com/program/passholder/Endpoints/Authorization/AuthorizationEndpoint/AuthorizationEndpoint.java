package com.program.passholder.Endpoints.Authorization.AuthorizationEndpoint;

import com.program.passholder.Authorization.ValidateAuthKey;
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

    @PostMapping("2FA")
    public ResponseEntity<Map<String, Object>> authorization(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AuthKeyDTO requestBody,
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
                String userEmail = jwtUtil.extractUsername(token);
                Optional<UserEntity> userEntity = userService.getEntityByMail(userEmail);
                long userId = getFromMail.getUserIdFromMail(userEmail);
                Optional<UserRoleEntity> entity = userRoleService.findByUserId(userId);
                int userRole=0;
                if(entity.isPresent()){
                    userRole = entity.get().getIdRole();
                }
                String authKey = requestBody.authKey;
                Boolean validationStatus = false;
                if(userEntity.isPresent()){
                    String userPhone = userEntity.get().getPhone();
                    int authMethode = userEntity.get().getNotificationMethod();
                    switch (authMethode) {
                        case 1:
                            if(validateAuthKey.validateAuthKey(userEmail, authKey)){
                                validationStatus = true;
                            } else{
                                validationStatus = false;
                            }
                            break;
                        case 2:
                            if(smsVerifyService.verifyCode(userPhone, authKey)){
                                validationStatus = true;
                            }else{
                                validationStatus = false;
                            }
                            break;
                    }
                }
                if(validationStatus){    //weryfikacja poprawności podanego kodu 2fa
                    setNewLog.setLog(3,ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "success", "role", userRole));
                }else{
                    setNewLog.setLog(4,ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed"));
                }
            }
        }
        setNewLog.setLog(12,ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
