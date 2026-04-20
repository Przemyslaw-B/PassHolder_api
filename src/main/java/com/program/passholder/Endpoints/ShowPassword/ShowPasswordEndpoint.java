package com.program.passholder.Endpoints.ShowPassword;

import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Endpoints.CreateNewAccount.NewUserDTO;
import com.program.passholder.Endpoints.GetUserStorageEndpoint.LoadUserStorage;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ShowPasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    PasswordService passwordService;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserService userService;

    @GetMapping("/showPassword")
    public ResponseEntity<Map<String, Object>> getUserStorageEndpoint(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ShowPasswordDTO request){
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String user = jwtUtil.extractUsername(token);
                String securityCode = request.securityCode;
                long userId = getFromMail.getUserIdFromMail(user);
                long passwordId = request.passwordId;
                PasswordEntity entityPassword= passwordService.getEntityById(passwordId).get();
                String securityPassword = userService.getSecurityPasswordById(userId);
                if(entityPassword.getUserId().equals(userId)){
                    String password = entityPassword.getPassword();
                    if(securityPassword != null && securityPassword.equals(securityCode)){
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Validated", "password", password));
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "wrong security password"));
                } else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "no permission"));
                }
            } else{
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Invalid token"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
