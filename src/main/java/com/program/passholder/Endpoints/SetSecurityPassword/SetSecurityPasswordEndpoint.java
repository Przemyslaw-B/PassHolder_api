package com.program.passholder.Endpoints.SetSecurityPassword;

import com.program.passholder.Database.Querry.User.User.SetSecurityPassword;
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
                String oldSecurityPassword = request.oldSecurityPassword;
                String newSecurityPassword = request.newSecurityPassword;
                String userSecurityPassword = userService.getSecurityPasswordById(userId);
                if(newSecurityPassword == null && newSecurityPassword.equals("")){
                    //TODO logs - nie podano nowego hasła
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "new security password is required"));
                }
                // Dla zmiany starego hasła
                if(userId > 0 && userSecurityPassword != null){
                    if(request.oldSecurityPassword == null || request.oldSecurityPassword.equals("")){
                        //TODO logs - nie podano starego hasła
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "old security password is required"));
                    }
                    if(userSecurityPassword.equals(oldSecurityPassword)){
                        userService.setSecurityPassword(userId, newSecurityPassword);   //ustawienie nowego hasła
                        //TODO logs - ustawinie hasła
                    } else {
                        //TODO logs - podano błędne stare hasło
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "old security password is wrong"));
                    }
                } else{
                    //Dla ustawienia nowego hasła
                    userService.setSecurityPassword(userId, newSecurityPassword);
                    //TODO logs - ustawiono nowe hasło bezpieczeństwa
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok"));
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
