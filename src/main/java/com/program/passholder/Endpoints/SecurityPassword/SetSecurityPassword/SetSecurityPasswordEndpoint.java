package com.program.passholder.Endpoints.SecurityPassword.SetSecurityPassword;

import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SetSecurityPasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;

    @PostMapping("/setSecurityPassword")
    public ResponseEntity<Map<String, Object>> setSecurityPasswordEndpoint(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SecurityPasswordDTO request) {
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userEmail = jwtUtil.extractUsername(token);
                long userId = userService.getUserIdByMail(userEmail);
                String newSecurityPassword = request.newSecurityPassword;
                String userSecurityPassword = userService.getSecurityPasswordById(userId);
                if(newSecurityPassword != null && !newSecurityPassword.equals("")) {
                    userService.setSecurityPassword(userId, newSecurityPassword);
                } else{
                    //TODO logs - nie podano nowego hasła
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "new security password is required"));
                }
            } else{
                //TODO logs - nieprawidłowy token
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Invalid token"));
            }
        }
        //TODO logs - brak tokenu
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
