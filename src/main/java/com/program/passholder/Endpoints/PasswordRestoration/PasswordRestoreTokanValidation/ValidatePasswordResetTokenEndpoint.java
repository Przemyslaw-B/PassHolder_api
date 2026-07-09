package com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreTokanValidation;

import com.program.passholder.Database.Querry.AuditLogs.Logs.LogEntity;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
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

import java.util.*;

@RestController
@RequestMapping("/api")
public class ValidatePasswordResetTokenEndpoint {
    @Autowired
    UserService userService;
    @Autowired
    ProceedPasswordRestoringProcess restorePassword;

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
            String savedPasswordToken = userService.getPasswordResetTokenByMail(request.token);
            if(request.token.equals(savedPasswordToken)){
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", true));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false));
    }
}
