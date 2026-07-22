package com.program.passholder.Endpoints.Storage.ShowPassword;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Endpoints.Storage.SaveNewUserStorageRecord.StorageRecordDTO;
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
public class ShowPasswordEndpoint {
    @Autowired
    UserService userService;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/showPassword")
    public ResponseEntity<Map<String, Object>> showPasswordEndpoint(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ShowPasswordDTO requestBody,
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
                Optional<UserEntity> userEntity = userService.getEntityByid(userId);
                if(userEntity.isPresent()){
                    setNewLog.setLog(26, ip, userId, userId, requestBody.recordId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "success"));
                }
            }
        }
        //setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
