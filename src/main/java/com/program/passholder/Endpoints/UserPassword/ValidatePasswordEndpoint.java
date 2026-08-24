package com.program.passholder.Endpoints.UserPassword;

import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.LoginProcessing.LoginCredentialsProcessing.ValidationUser;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ValidatePasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ValidationUser validationUser;

    @PostMapping("/validatePassword")
    public ResponseEntity<Map<String, Object>> validatePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ValidatePasswordDTO requestBody,
            HttpServletRequest httpRequest) {
        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userMail = jwtUtil.extractUsername(token);
                if(requestBody.password!=null && validationUser.validateUser(userMail, requestBody.password)){
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "validated", true));
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "validated", false));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
