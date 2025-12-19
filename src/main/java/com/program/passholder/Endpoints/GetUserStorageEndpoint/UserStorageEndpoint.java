package com.program.passholder.Endpoints.GetUserStorageEndpoint;

import com.program.passholder.Database.Querry.User.User.GetUserFromMail;
import com.program.passholder.Endpoints.LoginCredentialsReceivingEndpoint.LoginRequest;
import com.program.passholder.Login.LoginCredentialsProcessing.ValidationUser;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/recieveStorage")
    public ResponseEntity<Map<String, Object>> getUserStorageEndpoint(
            @RequestHeader("Authorization") String authHeader){
        System.out.println("getUserStorageEndpoint..");
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            System.out.println("token:"+token);
            if(token!=null && jwtUtil.validateToken(token)){
                String user = jwtUtil.extractUsername(token);
                //TODO zamiana storage na json i wysłanie do usera
                //String json = loadUserStorage.loadUserStorage(user);
                //System.out.println("Otrzymany json na wyjście api:" + json);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "storage", loadUserStorage.loadUserStorage(user)));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
