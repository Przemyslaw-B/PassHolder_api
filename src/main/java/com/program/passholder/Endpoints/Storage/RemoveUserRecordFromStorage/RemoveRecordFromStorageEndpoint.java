package com.program.passholder.Endpoints.Storage.RemoveUserRecordFromStorage;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Password.RemoveFromStorage.RemoveRecordFromStorage;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserRepository;
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
public class RemoveRecordFromStorageEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RemoveRecordFromStorage removeRecordFromStorage;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;

    @PostMapping("/RemoveRecordFromStorage")
    public ResponseEntity<Map<String, Object>> removeRecordFromStorage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RecordToRemoveDTO requestBody,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(token!= null && jwtUtil.validateToken(token)) {
                String userEmail = jwtUtil.extractUsername(token);
                UserEntity user = userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                long userId=user.getId();

                Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
                if(userRole.isEmpty() || userRole.get() < 1){
                    setNewLog.setLog(16, ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
                }

                long recordId=requestBody.recordId;
                //System.out.println("Otrzymano request usunięcia rekordu: " + recordId);
                removeRecordFromStorage.removeRecordFromStorage(userId, recordId);
                setNewLog.setLog(8, ip, userId, userId, recordId);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Success"));
            }
        }
        setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
