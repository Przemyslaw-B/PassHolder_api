package com.program.passholder.Endpoints.UserStorageCredentialUpdate;

import com.program.passholder.Database.Querry.Password.PasswordRepository;
import com.program.passholder.Database.Querry.Password.SetStorage.CreateNewRecordForUser;
import com.program.passholder.Database.Querry.Password.UpdateStorage.UpdatePasswordRecord;
import com.program.passholder.Database.Querry.User.User.GetUserFromMail;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Endpoints.CreateNewAccount.NewUserDTO;
import com.program.passholder.Endpoints.SaveNewUserStorageRecord.StorageRecordDTO;
import com.program.passholder.Session.JwtUtil;
import com.program.passholder.Storage.StoragedCredentialModification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserCredentialUpdate {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UpdatePasswordRecord updatePasswordRecord;
    @Autowired
    GetUserFromMail getUserFromMail;
    @Autowired
    StoragedCredentialModification storagedCredentialModification;

    @PostMapping("/ModifyUserCredential")
    public ResponseEntity<Map<String, String>> userCredentialUpdate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserStorageCredentialDTO requestBody) {
        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userEmail = jwtUtil.extractUsername(token);
                long userId = getUserFromMail.getUserIdFromMail(userEmail);
                long recordId = requestBody.recordId;
                String newUrl = requestBody.url;
                String newLogin = requestBody.login;
                String newPassword = requestBody.password;
                int newRotation = requestBody.rotation;
                Boolean result = storagedCredentialModification.updateCredential(recordId, userId, newUrl, newLogin, newPassword, newRotation);
                if(result){
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "changes", "success"));

                } else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "changes", "failed"));

                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
