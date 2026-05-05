package com.program.passholder.Endpoints.SecurityPassword.GetSecurityPassword;

import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GetSecurityPasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserService userService;

    @GetMapping("/getSecurityPassword")
    public ResponseEntity<Map<String, Object>> getSecurityPassword(
            @RequestHeader("Authorization") String authHeader){
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            //System.out.println("Użytkownik chce pobrać swoje hasło bezpieczeństwa..");
            String token = authHeader.substring(7);
            if(jwtUtil.validateToken(token)) {
                String userMail = jwtUtil.extractUsername(token);
                long userId = getFromMail.getUserIdFromMail(userMail);
                String password = userService.getSecurityPasswordById(userId);
                Map<String, String> map = new HashMap<>();
                map.put("securityPassword", password);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "map", map));
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "token invalid"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
