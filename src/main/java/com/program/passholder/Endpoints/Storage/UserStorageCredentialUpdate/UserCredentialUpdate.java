package com.program.passholder.Endpoints.Storage.UserStorageCredentialUpdate;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Password.UpdateStorage.UpdatePasswordRecord;
import com.program.passholder.Database.Querry.User.User.GetUserFromMail;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
import com.program.passholder.Storage.StoragedCredentialModification;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;

    @PostMapping("/ModifyUserCredential")
    public ResponseEntity<Map<String, String>> userCredentialUpdate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserStorageCredentialDTO requestBody,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userEmail = jwtUtil.extractUsername(token);
                long userId = getUserFromMail.getUserIdFromMail(userEmail);
                Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
                if(userRole.isEmpty() || userRole.get() < 1){
                    setNewLog.setLog(16, ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
                }

                long recordId = requestBody.recordId;
                String newUrl = requestBody.url;
                String newLogin = requestBody.login;
                String newPassword = requestBody.password;
                //int newRotation = requestBody.rotation;
                Boolean result = storagedCredentialModification.updateCredential(recordId, userId, newUrl, newLogin, newPassword);// , newRotation);
                if(result){
                    if(!newLogin.isEmpty()){
                        setNewLog.setLog(9,ip,userId,userId, recordId);
                    }
                    if(!newUrl.isEmpty()){
                        setNewLog.setLog(10,ip,userId,userId, recordId);
                    }
                    if(!newPassword.isEmpty()){
                        setNewLog.setLog(11,ip,userId,userId, recordId);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "changes", "success"));
                } else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "changes", "failed"));

                }
            }
        }
        setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
