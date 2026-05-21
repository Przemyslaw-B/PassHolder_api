package com.program.passholder.Endpoints.Authorization.AuthorizationChangeAccessVerification;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Endpoints.Login.LoginCredentialsReceivingEndpoint.LoginRequest;
import com.program.passholder.Login.LoginCredentialsProcessing.ValidationUser;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthorizationChangeAccessVerification {
    @Autowired
    UserService userService;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ValidationUser validationUser;

    @PostMapping("/changeAuthMethodeVerifyUser")

    public ResponseEntity<Map<String, Object>> authorizationChangeAccessVerification(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AuthChangeAccessVerificationDTO request,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            Optional<UserEntity> userEntity = userService.getEntityByMail(userMail);
            if(userEntity.isPresent()) {
                long userId = userEntity.get().getId();
                String pass= request.password;
                if(validationUser.validateUser(userMail, pass)){
                    setNewLog.setLog(27,ip,userId,userId);
                    return ResponseEntity.ok(Map.of("status", "Validated", "success", true));
                }
                setNewLog.setLog(28,ip,userId,userId);
                return ResponseEntity.ok(Map.of("status", "ok", "success", false));
            }
        }
        return ResponseEntity.ok(Map.of("status", "Invalid"));
    }

}
