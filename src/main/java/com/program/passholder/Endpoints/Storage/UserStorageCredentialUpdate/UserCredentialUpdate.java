package com.program.passholder.Endpoints.Storage.UserStorageCredentialUpdate;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordRepository;
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
    @Autowired
    PasswordRepository passwordRepository;

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
                Optional<PasswordEntity> entity = passwordRepository.findByIdAndUserId(recordId, userId);
                if(!entity.isPresent()){
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "changes", "failed"));
                }
                String oldUrl = entity.get().getUrl();
                String oldLogin = entity.get().getLogin();

                Boolean result = storagedCredentialModification.updateCredential(recordId, userId, newUrl, newLogin, newPassword);
                if(result){
                    if(!newLogin.equals(oldLogin)){
                        setNewLog.setLog(16,ip,userId,userId, recordId);
                    }
                    if(newUrl.equals(oldUrl)){
                        setNewLog.setLog(15,ip,userId,userId, recordId);
                    }
                    if(!newPassword.isEmpty()){
                        setNewLog.setLog(17,ip,userId,userId, recordId);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "changes", "success"));
                } else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "changes", "failed"));

                }
            }
        }
        //setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
