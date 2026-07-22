package com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreInit;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
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
public class RestorePasswordEndpoint {

    @Autowired
    ProceedPasswordRestoringProcess restorePassword;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    UserService userService;

    @PostMapping("/restorePassword")
    public ResponseEntity<Map<String, Object>> restorePasswordEndpoint(
            @RequestBody RestorePasswordDTO request,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        //System.out.println("Request zmiany hasła użytkownika od ip: " + ip);

        String requestedEmail = "";
        if(!request.email.isEmpty()){
            requestedEmail = request.email;
            restorePassword.proceedPasswordRestoringProcess(requestedEmail);
        }
        //Wyciągnięcie usera jeśli istnieje użytkownik z takim email
        Optional<UserEntity> userEntity = userService.getEntityByMail(requestedEmail);
        if(userEntity.isPresent()){
            long userId = userEntity.get().getId();
            setNewLog.setLog(6,ip, userId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of());
    }
}
