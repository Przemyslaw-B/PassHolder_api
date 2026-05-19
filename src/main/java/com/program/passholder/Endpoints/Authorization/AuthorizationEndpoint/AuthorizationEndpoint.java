package com.program.passholder.Endpoints.Authorization.AuthorizationEndpoint;

import com.program.passholder.Authorization.ValidateAuthKey;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthorizationEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ValidateAuthKey validateAuthKey;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;

    @PostMapping("2FA")
    public ResponseEntity<Map<String, Object>> authorization(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AuthKeyDTO requestBody,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (token != null && jwtUtil.validateToken(token)) {
                String userEmail = jwtUtil.extractUsername(token);
                long userId = getFromMail.getUserIdFromMail(userEmail);
                Optional<UserRoleEntity> entity = userRoleService.findByUserId(userId);
                int userRole=0;
                if(entity.isPresent()){
                    userRole = entity.get().getIdRole();
                }
                String authKey = requestBody.authKey;
                //System.out.println("AUTH KEY: " + authKey);
                if(validateAuthKey.validateAuthKey(userEmail, authKey)){    //weryfikacja poprawności podanego kodu 2fa
                    setNewLog.setLog(3,ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "success", "role", userRole));
                }else{
                    setNewLog.setLog(4,ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed"));
                }
                //
                //return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "success"));
            }
        }
        setNewLog.setLog(12,ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
