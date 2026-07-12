package com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreSaveNewPassword;

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

        if(!request.email.isEmpty() && !request.newPassword.isEmpty() && !request.authCode.isEmpty() && !request.passwordChangeToken.isEmpty()){
            Optional<UserEntity> userEntity = userService.getEntityByMail(request.email);
            if(userEntity.isPresent()){
                String userAuthCode = userEntity.get().getAuthKey();
                String userResetPasswordToken = userEntity.get().getPasswordResetToken();
                if(!userAuthCode.isEmpty() && !userResetPasswordToken.isEmpty()){
                    if(userAuthCode.equals(request.authCode) && userResetPasswordToken.equals(request.passwordChangeToken)){
                        String hashedNewPassword = hash.passwordEncoder().encode(request.newPassword);
                        userService.setNewAccountPassword(request.email, hashedNewPassword);
                        //TODO zapis w logach
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", true, "message", "Hasło zmienione pomyślnie"));
                    }
                }
            }
        }
        //TODO zapis w logach
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false, "message", "Błąd zmiany hasła."));
    }
}
