package com.program.passholder.Endpoints.UserSettingsEndpoints.UserPhone.UserPhone.ActivatePhone;

import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ActivatePhoneEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;

    @PostMapping("/activatePhone")
    public ResponseEntity<Map<String, Object>> activate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ActivatePhoneDTO request){
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)) {
                if(request.phone!=null && request.activationKey!=null) {
                    String email = jwtUtil.extractUsername(token);
                    String userKey = userService.getAuthKeyByMail(email);
                    if (userKey != null) {
                        if (userKey.equals(request.activationKey)) {
                            userService.setUserPhone(email, request.phone);
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", true));
                        } else {
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false));
                        }
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "empty key","success", false));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid","success", false));
    }
}

