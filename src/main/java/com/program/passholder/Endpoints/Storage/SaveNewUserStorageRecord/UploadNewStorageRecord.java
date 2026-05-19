package com.program.passholder.Endpoints.Storage.SaveNewUserStorageRecord;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Password.SetStorage.CreateNewRecordForUser;
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
public class UploadNewStorageRecord {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CreateNewRecordForUser createNewRecordForUser;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;

    @PostMapping("/UploadNewRecord")
    public ResponseEntity<Map<String, Object>> uploadNewStorageRecord(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody StorageRecordDTO requestBody,
            HttpServletRequest httpRequest) {

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
                UserEntity user = userRepository.findByEmail(userEmail)
                                .orElseThrow(()-> new RuntimeException("User not found"));  //Jeśli user nie istnieje rzuć wyjątek
                long userId=user.getId();

                Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
                if(userRole.isEmpty() || userRole.get() < 1){
                    setNewLog.setLog(16, ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
                }

                long idCloud= createNewRecordForUser.setNewPasswordRecordForUser(userId, requestBody.url, requestBody.access_login, requestBody.access_pwd, requestBody.modification_date);
                setNewLog.setLog(7, ip, userId, userId, idCloud);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "id_cloud", idCloud));
            }
        }
        setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
