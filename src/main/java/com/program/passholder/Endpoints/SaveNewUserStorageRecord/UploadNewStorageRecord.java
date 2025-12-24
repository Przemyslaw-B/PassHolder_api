package com.program.passholder.Endpoints.SaveNewUserStorageRecord;

import com.program.passholder.Database.Querry.Password.SetStorage.CreateNewRecordForUser;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Endpoints.GetUserStorageEndpoint.LoadUserStorage;
import com.program.passholder.Session.JwtUtil;
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

    @PostMapping("/UploadNewRecord")
    public ResponseEntity<Map<String, Object>> uploadNewStorageRecord(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody StorageRecordDTO requestBody) {
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userEmail = jwtUtil.extractUsername(token);
                UserEntity user = userRepository.findByEmail(userEmail)
                                .orElseThrow(()-> new RuntimeException("User not found"));  //Jeśli user nie istnieje rzuć wyjątek
                long userId=user.getId();
                long idCloud= createNewRecordForUser.setNewPasswordRecordForUser(userId, requestBody.url, requestBody.access_login, requestBody.access_pwd, requestBody.exp_date, requestBody.modification_date);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "id_cloud", idCloud));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
