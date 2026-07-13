package com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreSaveNewPassword;

import com.program.passholder.Authorization.ValidateAuthKey;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Encryption.Encoder;
import com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreInit.RestorePasswordDTO;
import com.program.passholder.PasswordRestore.ProceedPasswordRestoringProcess;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println("Zmiana hasła usera na nowe..");
        if(!request.email.isEmpty() && !request.newPassword.isEmpty() && !request.authCode.isEmpty() && !request.passwordChangeToken.isEmpty()){
            Optional<UserEntity> userEntity = userService.getEntityByMail(request.email);
            if(userEntity.isPresent()){
                System.out.println("Email prawidłowy, wykryto UserEntity!");
                String userAuthCode = userEntity.get().getAuthKey();
                String userResetPasswordToken = userEntity.get().getPasswordResetToken();
                if(!userAuthCode.isEmpty() && !userResetPasswordToken.isEmpty()){
                    if(validateAuthKey.validate(request.email, request.authCode) && userResetPasswordToken.equals(request.passwordChangeToken)){
                        String hashedNewPassword = hash.passwordEncoder().encode(request.newPassword);
                        userService.setNewAccountPassword(request.email, hashedNewPassword);
                        //TODO zapis w logach
                        System.out.println("Hasło zmienione pomyślnie!");
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", true, "message", "Hasło zmienione pomyślnie"));
                    } else{
                        System.out.println("authCode lub ResetPasswordToken jest nieprawidłowe!");
                        System.out.println("otrzymany authCode: " + request.authCode);
                        System.out.println("authCode z bazy: " + userAuthCode);
                        System.out.println("authCode takie samo?: " + userAuthCode.equals(request.authCode));
                        System.out.println("otrzymany reset Token: " + request.passwordChangeToken);
                        System.out.println("reset password Token z Bazy: " + userResetPasswordToken);
                        System.out.println("PasswordResetToken taki sam?: " + userResetPasswordToken.equals(request.passwordChangeToken));
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false, "message", "Błędny kod autoryzacji."));
                    }
                } else{
                    System.out.println("otrzymany AuthCode lub ResetPasswordToken jest .isEmpty == True");
                }
            } else{
                System.out.println("Email nieprawidłowy, NIE wykryto UserEntity!");
            }
        }
        //TODO zapis w logach
        System.out.println("Nie udało się zmienić hasła..");
        System.out.println("Otrzymany email: " + request.email);
        System.out.println("Otrzymany reset token: " + request.passwordChangeToken);
        System.out.println("Otrzymany auth code: " + request.authCode);
        System.out.println("Otrzymane nowe hasło: " + request.newPassword);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false, "message", "Błąd zmiany hasła."));
    }
}
