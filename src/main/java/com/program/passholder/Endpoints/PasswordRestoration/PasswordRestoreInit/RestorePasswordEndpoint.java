package com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreInit;

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

@RestController
@RequestMapping("/api")
public class RestorePasswordEndpoint {

    @Autowired
    ProceedPasswordRestoringProcess restorePassword;

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

        String requestedEmail = "";
        if(!request.email.isEmpty()){
            requestedEmail = request.email;
            restorePassword.proceedPasswordRestoringProcess(requestedEmail);
        }
        //TODO zdarzenie żądania zmiany hasła
        return ResponseEntity.status(HttpStatus.OK).body(Map.of());
    }
}
