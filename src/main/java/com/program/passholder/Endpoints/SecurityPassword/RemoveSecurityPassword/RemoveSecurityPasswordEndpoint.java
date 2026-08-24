package com.program.passholder.Endpoints.SecurityPassword.RemoveSecurityPassword;

import com.program.passholder.Authentication.ValidateAuthKey;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import com.program.passholder.Storage.RemoveStorage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RemoveSecurityPasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    ValidateAuthKey validateAuthKey;
    @Autowired
    RemoveStorage removeStorage;


    @PostMapping("/removeSecurityPassword")
    public ResponseEntity<Map<String, Object>> removePasswordEndpoint(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RemoveSecurityPasswordDTO request,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userEmail = jwtUtil.extractUsername(token);
                long userId = userService.getUserIdByMail(userEmail);
                if(!request.code.isEmpty()){
                    boolean auth = validateAuthKey.validate(userEmail, request.code);
                    if(auth){
                        userService.removeUserSecurityPassword(userId);
                        removeStorage.removeStorage(userId);
                        setNewLog.setLog(13, ip, userId, userId);   //logowanie usunięcia hasła bezpieczeństwa
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", true));
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
