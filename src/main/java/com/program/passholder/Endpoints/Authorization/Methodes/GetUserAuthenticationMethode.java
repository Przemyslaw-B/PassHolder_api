package com.program.passholder.Endpoints.Authorization.Methodes;

import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesEntity;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesRepository;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GetUserAuthenticationMethode {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationMethodesService authenticationMethodesService;

    @GetMapping("/getUserAuthenticationMethode")
    public ResponseEntity<Map<String, Object>> getAllMethodes(
            @RequestHeader("Authorization") String authHeader){
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            //System.out.println("API ROLE ENDPOINT!");
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);
            //System.out.println("odczytany userID: " + userId);
            Optional<UserEntity> userEntity = userService.getEntityByid(userId);
            if(userEntity.isPresent()){
                //System.out.println("Odnaleziono userEntity");
                int userAuthMethode = userEntity.get().getNotificationMethod();
                Optional<AuthenticationMethodesEntity> methodeEntity = authenticationMethodesService.getMethodeById(userAuthMethode);
                if(methodeEntity.isPresent()){
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "methode", methodeEntity.get().getName(), "methodeId", methodeEntity.get().getId()));
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "methode", "not found"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "message", "user not found"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}

