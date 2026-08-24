package com.program.passholder.Endpoints.UserSettingsEndpoints.UserPhone.UserPhone.RequestPhoneActivationKey;

import com.program.passholder.Authentication.GenerateAuthKey;
import com.program.passholder.Authentication.ProceedAuth;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    private GetFromMail getFromMail;

    @PostMapping("/requestPhoneActivationKey")
    public ResponseEntity<Map<String, Object>> activate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserPhoneDTO request,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)) {
                if(request.phone!=null){
                    String email = jwtUtil.extractUsername(token);
                    long userId = getFromMail.getUserIdFromMail(email);
                    proceedAuth.sendAuthKeySms(request.phone);
                    setNewLog.setLog(27, ip, userId);   //loguj wysłanie kodu aktywacyjnego sms
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok"));
                } else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "empty number"));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
