package com.program.passholder.Endpoints.Login.LoginCredentialsReceivingEndpoint;

import com.program.passholder.Authentication.IsAuthorized;
import com.program.passholder.Authentication.ProceedAuth;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.*;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.LoginProcessing.LoginCredentialsProcessing.ValidationUser;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginValidationEndpoint {
    @Autowired
    GetUserFromMail getUserFromMail;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ProceedAuth proceedAuth;
    @Autowired
    IsAuthorized isAuthorized;
    @Autowired
    UserService userService;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    ValidationUser validationUser;

    @PostMapping("/userValidation")
public ResponseEntity<Map<String, Object>> isUserValidEndpoint(
        @RequestBody LoginRequest request,
        HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        String password = request.getPassword();
        String email = request.getEmail();
        if(!userService.isUserExist(email)){
            //return ResponseEntity.notFound(Map.of("status", "Invalid"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Invalid"));
        }
        String username = getUserFromMail.getUserFromMail(email);
        long userId = getFromMail.getUserIdFromMail(email);
        Optional<UserEntity> userEntity = userService.getEntityByid(userId);

        Date currentDate = new Date();
        if(userEntity.isPresent()){
            Optional<Date> accountLock = userService.getLockUntil(userId);
            if(accountLock.isPresent() &&  accountLock.get().after(currentDate)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("status", "Invalid", "reason","Blocked"));
            }
        }

        if (userEntity.isPresent()){
            if(validationUser.validateUser(email, password)){
                int userAuthMethode = userEntity.get().getNotificationMethod();
                //String token = jwtUtil.generateToken(email);
                boolean authorized = isAuthorized.isAuthorized(email);
                //String auth = Boolean.toString(authorized);
                proceedAuth.proceed(email); //wyślij wiadomość do usera
                setNewLog.setLog(3,ip, userId, userId); //logowanie użytkownika
                userService.setFailedAttemps(userId, 0);
                return ResponseEntity.ok(Map.of("status", "Validated","authMethode", userAuthMethode));
            }
            //NIEUDANA PRÓBA LOGOWANIA
            setNewLog.setLog(29,ip, userId, userId); //nieudane logowanie użytkownika
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
                userService.setFailedAttemps(userId, 0);    //Po zablokowaniu konta wyzerowanie prób
                setNewLog.setLog(30, ip, userId, userId);    //Blokada konta
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("status", "Invalid", "reason","Blocked"));
            }
            //return ResponseEntity.ok(Map.of("status", "Invalid", "reason", "Błędne dane"));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid", "reason","Blocked"));
        }
        //setNewLog.setLog(4,ip, userId);
        //return ResponseEntity.ok(Map.of("status", "Invalid"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "Invalid"));
    }

}
