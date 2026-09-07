package com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreSaveNewPassword;

import com.program.passholder.Authentication.ValidateAuthKey;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordService;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Encryption.Encoder;
import com.program.passholder.Storage.RemoveStorage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PasswordRestoreSaveNewPasswordEndpoint {
    @Autowired
    Encoder hash;
    @Autowired
    UserService userService;
    @Autowired
    ValidateAuthKey validateAuthKey;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    PasswordService passwordService;
    @Autowired
    RemoveStorage removeStorage;


    @PostMapping("/restorePassword/saveNewPassword")
    public ResponseEntity<Map<String, Object>> passwordRestoreSaveNewPasswordEndpoint(
            @RequestBody PasswordRestoreSaveNewPasswordDTO request,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        System.out.println("Zmiana sejfu usera..");

        //System.out.println("Zmiana hasła usera na nowe..");
        if(!request.email.isEmpty() && !request.newPassword.isEmpty() && !request.authCode.isEmpty() && !request.passwordChangeToken.isEmpty()){
            Optional<UserEntity> userEntity = userService.getEntityByMail(request.email);
            if(userEntity.isPresent()){
                long userId = userEntity.get().getId();
                int failedAttemps = 1;
                Optional<Integer> userFailedAttemps = userService.getFailedAttemps(userId);
                if (userFailedAttemps.isPresent()) {
                    failedAttemps = userFailedAttemps.get();
                }
                Date currentDate = new Date();
                Optional<Date> accountLock = userService.getLockUntil(userId);
                if (accountLock.isPresent() && accountLock.get().after(currentDate)) {
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "success", false, "message", "Zbyt wiele prób. Proszę spróbować później."));
                }

                String userAuthCode = userEntity.get().getAuthKey();
                String userResetPasswordToken = userEntity.get().getPasswordResetToken();
                if(!userAuthCode.isEmpty() && !userResetPasswordToken.isEmpty()){

                    System.out.println("request authCode validation: " + validateAuthKey.validate(request.email, request.authCode));
                    System.out.println("request resetToken validation: " + userResetPasswordToken.equals(request.passwordChangeToken));

                    if(validateAuthKey.validate(request.email, request.authCode) && userResetPasswordToken.equals(request.passwordChangeToken)){
                        System.out.println("Wszystkie tokeny poprawne!");
                        String hashedNewPassword = hash.passwordEncoder().encode(request.newPassword);
                        userService.setNewAccountPassword(request.email, hashedNewPassword);
                        if(request.dataRemove==true){
                            System.out.println("Usuwanie storage..");
                            removeStorage.removeStorage(userId);
                        } else{
                            System.out.println("Podmiana storage");
                            for(PasswordEntity passwordEntity : request.storage) {
                                long passwordId = passwordEntity.getId();
                                String newUrl = passwordEntity.getUrl();
                                System.out.println("nowy URL: " + newUrl);
                                String newLogin = passwordEntity.getLogin();
                                System.out.println("nowy URL: " + newLogin);
                                passwordService.setStorageUrl(passwordId, userId, newUrl);
                                passwordService.setStorageLogin(passwordId, userId, newLogin);
                            }
                        }
                        //System.out.println("Hasło zmienione pomyślnie!");
                        System.out.println("Zmiana hasla zakonczona.");
                        setNewLog.setLog(7,ip, userId, userId); //log pomyślnie zmienionego hasła
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", true, "message", "Hasło zmienione pomyślnie"));
                    } else{
                        failedAttemps += 1;
                        if (failedAttemps >= 5) {
                            Date newLockDate = new Date(System.currentTimeMillis() + 5 * 60 * 1000);    //Data blokady Bieżąca data + 5 minut
                            userService.setLockedUntil(userId, newLockDate);
                            userService.setFailedAttemps(userId, 0);    //po blokadzie konta wyzerowac próby
                            setNewLog.setLog(30, ip, userId, userId);    //Blokada konta
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "success", false, "message", "Zbyt wiele prób. Proszę spróbować później."));
                        }
                        userService.setFailedAttemps(userId, failedAttemps);
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false, "message", "Błędny kod autoryzacji."));
                    }
                } else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false, "message", "Błąd zmiany hasła."));
                }
            } else{
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false, "message", "Błąd zmiany hasła."));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false, "message", "Błąd zmiany hasła."));
    }
}
