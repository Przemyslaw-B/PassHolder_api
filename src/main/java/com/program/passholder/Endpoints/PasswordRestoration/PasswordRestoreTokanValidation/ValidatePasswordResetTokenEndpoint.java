package com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreTokanValidation;

import com.program.passholder.Authorization.ProceedAuth;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogEntity;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreInit.RestorePasswordDTO;
import com.program.passholder.PasswordRestore.ProceedPasswordRestoringProcess;
import com.program.passholder.PasswordRestore.ValidateResetPasswordToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ValidatePasswordResetTokenEndpoint {
    @Autowired
    ValidateResetPasswordToken validateResetPasswordToken;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    UserService userService;
    @Autowired
    ProceedAuth proceedAuth;

    @PostMapping("/restorePassword/validateToken")
    public ResponseEntity<Map<String, Object>> validatePasswordResetTokenEndpoint(
            @RequestBody ValidatePasswordResetTokenDTO request,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }


        if(!request.email.isEmpty() && !request.token.isEmpty()){
            Optional<UserEntity> userEntity = userService.getEntityByMail(request.email);
            if(userEntity.isPresent()){
                long userId = userEntity.get().getId();
                int failedAttemps = 1;
                Optional<Integer> userFailedAttemps = userService.getFailedAttemps(userId);
                if (userFailedAttemps.isPresent()) {
                    failedAttemps = userFailedAttemps.get();
                    Date currentDate = new Date();
                    Optional<Date> accountLock = userService.getLockUntil(userId);
                    if (accountLock.isPresent() && accountLock.get().after(currentDate)) {
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "success", false, "reason", "Blocked"));
                    }
                }

                if(validateResetPasswordToken.validatePasswordResetToken(request.email, request.token)){
                    int authMethode = userEntity.get().getNotificationMethod();
                    userService.setFailedAttemps(userId, 0);
                    setNewLog.setLog(23,ip, userId, userId);    //poprawny token LOG
                    proceedAuth.proceed(request.email);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", true, "authMethode", authMethode));
                } else{
                    setNewLog.setLog(22,ip, userId, userId);    //niepoprawny token LOG
                    failedAttemps += 1;
                    userService.setFailedAttemps(userId, failedAttemps);
                    if (failedAttemps >= 5) {
                        Date newLockDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);    //Data blokady Bieżąca data + 5 minut
                        userService.setLockedUntil(userId, newLockDate);
                        userService.setFailedAttemps(userId, 0);    //po blokadzie konta wyzerowac próby
                        setNewLog.setLog(30, ip, userId, userId);    //Blokada konta
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "success", false, "reason", "Blocked"));
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false));
    }
}
