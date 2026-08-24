package com.program.passholder.Endpoints.Authorization.SendAuthCode;

import com.program.passholder.Authentication.ProceedAuth;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SendAuthCodeEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserService userService;
    @Autowired
    ProceedAuth proceedAuth;

    @GetMapping("/getAuthCode")
    public ResponseEntity<Map<String, Object>> getAuthCode(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            Optional<UserEntity> userEntity = userService.getEntityByMail(userMail);
            if(userEntity.isPresent()) {
                long userId = getFromMail.getUserIdFromMail(userMail);
                int userAuthMethode = userEntity.get().getNotificationMethod();
                if(userAuthMethode == 1){
                    proceedAuth.sendKeyToPickedMethode(userMail, 1);
                } else if(userAuthMethode == 2){
                    proceedAuth.sendKeyToPickedMethode(userMail, 2);
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
