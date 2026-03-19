package com.program.passholder.Endpoints.UserSettingsEndpoints.PasswordRotation;


import com.program.passholder.Database.Querry.Rotation.SetDefaultUserRotation;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserDefaultRotation {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SetDefaultUserRotation setDefaultUserRotation;

    @PostMapping("/CredentialUserRotation")
    public ResponseEntity<Map<String, Object>> setUserCredentialRotation(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserRotationDTO requestBody){
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userEmail = jwtUtil.extractUsername(token);
                setDefaultUserRotation.setUserDefaultRotationByEmail(userEmail, requestBody.rotation);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
