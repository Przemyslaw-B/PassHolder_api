package com.program.passholder.Endpoints.UserSettingsEndpoints.UserPhone.UserPhone;

import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Endpoints.RSA.RsaRequest;
import com.program.passholder.Session.JwtUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GetUserPhoneEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;

    @PostMapping("/getUserPhone")
    public ResponseEntity<Map<String, String>> getKey(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (token != null && jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractUsername(token);
                Optional<UserEntity> entity = userService.getEntityByMail(email);
                if(entity.isPresent()){
                    String phone = entity.get().getPhone();
                    return ResponseEntity.ok(Map.of("status", "ok", "phone", phone));
                } else{
                    return ResponseEntity.ok(Map.of("status", "ok", "error", "user not found"));
                }
            }
        }
        return ResponseEntity.ok(Map.of("status", "Invalid", "publicKey", ""));
    }
}
