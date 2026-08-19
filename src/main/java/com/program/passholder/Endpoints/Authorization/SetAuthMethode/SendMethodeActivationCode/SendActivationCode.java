package com.program.passholder.Endpoints.Authorization.SetAuthMethode.SendMethodeActivationCode;

import com.program.passholder.Authorization.ProceedAuth;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesEntity;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesService;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SendActivationCode {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationMethodesService authenticationMethodesService;
    @Autowired
    ProceedAuth proceedAuth;

    @PostMapping("/sendMethodeActivationCode")
    public ResponseEntity<Map<String, Object>> sendActivationCode(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SendActivationCodeDTO request,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            Optional<UserEntity> entity = userService.getEntityByMail(userMail);
            List<AuthenticationMethodesEntity> methodes = authenticationMethodesService.getAllAuthenticationMethodes();
            if(entity.isPresent() && methodes !=null && !methodes.isEmpty()) {
                long userId = entity.get().getId();
                for(AuthenticationMethodesEntity methode : methodes) {
                    if(request.methode.equals(methode.getName())){
                        int methodeId = methode.getId();
                        proceedAuth.sendKeyToPickedMethode(userMail, methodeId);
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK","success", true, "atMethod", methodeId));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK","success", false, "error", "nie odczytano danych"));
        }
        //setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed"));
    }
}
