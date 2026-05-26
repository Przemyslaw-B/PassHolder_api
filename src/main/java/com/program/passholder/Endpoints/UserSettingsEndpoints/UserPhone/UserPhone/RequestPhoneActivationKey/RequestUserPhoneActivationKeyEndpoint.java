package com.program.passholder.Endpoints.UserSettingsEndpoints.UserPhone.UserPhone.RequestPhoneActivationKey;

import com.program.passholder.Authorization.GenerateAuthKey;
import com.program.passholder.Authorization.ProceedAuth;
import com.program.passholder.Database.Querry.User.Authentication.SetAuthKey;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RequestUserPhoneActivationKeyEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GenerateAuthKey generateAuthKey;
    @Autowired
    UserService userService;
    @Autowired
    ProceedAuth proceedAuth;

    @PostMapping("/requestPhoneActivationKey")
    public ResponseEntity<Map<String, Object>> activate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserPhoneDTO request){
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)) {
                if(request.phone!=null){
                    String email = jwtUtil.extractUsername(token);
                    proceedAuth.sendAuthKeySms(request.phone);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok"));
                } else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "empty number"));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
