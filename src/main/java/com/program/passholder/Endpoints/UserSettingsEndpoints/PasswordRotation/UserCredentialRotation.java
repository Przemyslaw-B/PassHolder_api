package com.program.passholder.Endpoints.UserSettingsEndpoints.PasswordRotation;

import com.program.passholder.Database.Querry.Rotation.SetCredentialRotation;
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
public class UserCredentialRotation {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SetCredentialRotation setCredentialRotation;

    @PostMapping("/DefaultUserRotation")
    public ResponseEntity<Map<String, Object>> setUserDefaultRotation(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserRotationDTO requestBody){
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userEmail = jwtUtil.extractUsername(token);
                setCredentialRotation.SetCredentialRotationById(requestBody.recordId, requestBody.rotation);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
