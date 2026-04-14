package com.program.passholder.Endpoints.RemoveUserRecordFromStorage;

import com.program.passholder.Database.Querry.Password.RemoveFromStorage.RemoveRecordFromStorage;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RemoveRecordFromStorageEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RemoveRecordFromStorage removeRecordFromStorage;

    @PostMapping("/RemoveRecordFromStorage")
    public ResponseEntity<Map<String, Object>> removeRecordFromStorage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RecordToRemoveDTO requestBody) {
        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(token!= null && jwtUtil.validateToken(token)) {
                String userEmail = jwtUtil.extractUsername(token);
                UserEntity user = userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                long userId=user.getId();
                long recordId=requestBody.recordId;
                System.out.println("Otrzymano request usunięcia rekordu: " + recordId);
                removeRecordFromStorage.removeRecordFromStorage(userId, recordId);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Success"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
