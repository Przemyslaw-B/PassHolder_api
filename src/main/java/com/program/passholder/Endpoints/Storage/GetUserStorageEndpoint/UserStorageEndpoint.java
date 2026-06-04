package com.program.passholder.Endpoints.Storage.GetUserStorageEndpoint;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.User.GetUserFromMail;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Login.LoginCredentialsProcessing.ValidationUser;
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
public class UserStorageEndpoint {
    @Autowired
    ValidationUser validationUser;
    @Autowired
    GetUserFromMail getUserFromMail;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    LoadUserStorage loadUserStorage;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;

    @GetMapping("/recieveStorage")
    public ResponseEntity<Map<String, Object>> getUserStorageEndpoint(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        //System.out.println("getUserStorageEndpoint..");
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            //System.out.println("token:"+token);
            if(token!=null && jwtUtil.validateToken(token)){
                String user = jwtUtil.extractUsername(token);
                long userId = getFromMail.getUserIdFromMail(user);

                //String json = loadUserStorage.loadUserStorage(user);
                //System.out.println("Otrzymany json na wyjście api:" + json);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "storage", loadUserStorage.loadUserStorage(user)));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
