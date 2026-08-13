package com.program.passholder.Endpoints.SecurityPassword.ResetSecurityPassword;

import com.program.passholder.Authorization.ValidateAuthKey;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordService;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Endpoints.SecurityPassword.RemoveSecurityPassword.RemoveSecurityPasswordDTO;
import com.program.passholder.Session.JwtUtil;
import com.program.passholder.Storage.StoragedCredentialModification;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ResetSecurityPasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    ValidateAuthKey validateAuthKey;
    @Autowired
    PasswordService passwordService;

    @PostMapping("/resetSecurityPassword")
    public ResponseEntity<Map<String, Object>> resetPasswordEndpoint(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ResetSecurityPasswordDTO request,
            HttpServletRequest httpRequest){
        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userEmail = jwtUtil.extractUsername(token);
            long userId = userService.getUserIdByMail(userEmail);
            if (token != null && jwtUtil.validateToken(token)) {
                if(request.code!=null && request.newSecurityPassword!=null) {
                    boolean auth = validateAuthKey.validate(userEmail, request.code);
                    Optional<UserEntity> entity = userService.getEntityByid(userId);
                    if(auth && entity.isPresent()) {
                        //String oldSecurityPassword = entity.get().getSecurity_password();
                        String newSecurityPassword = request.newSecurityPassword;
                        boolean resultSecPass = userService.setSecurityPassword(userId, newSecurityPassword);
                        if(resultSecPass && !request.storage.isEmpty()){
                            for(PasswordEntity passwordEntity : request.storage) {
                                long passwordId = passwordEntity.getId();
                                String newPass = passwordEntity.getPassword();
                                passwordService.setStoragePassword(passwordId, userId, newPass);
                            }
                            setNewLog.setLog(12, ip, userId, userId);   //logowanie zmiany hasła bezpieczeństwa
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", "true"));
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", "false"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
