package com.program.passholder.Endpoints.AuthorizationEndpoint;

import com.program.passholder.Authorization.ValidateAuthKey;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthorizationEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ValidateAuthKey validateAuthKey;

    @PostMapping("2FA")
    public ResponseEntity<Map<String, String>> authorization(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AuthKeyDTO requestBody) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (token != null && jwtUtil.validateToken(token)) {
                String userEmail = jwtUtil.extractUsername(token);
                String authKey = requestBody.authKey;
                //System.out.println("AUTH KEY: " + authKey);
                if(validateAuthKey.validateAuthKey(userEmail, authKey)){    //weryfikacja poprawności podanego kodu 2fa
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "success"));
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
