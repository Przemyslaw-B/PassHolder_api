package com.program.passholder.Endpoints.UserSettingsEndpoints.UserPhone.UserPhone.ChangeNumber;

import com.program.passholder.Authorization.ValidateAuthKey;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Endpoints.UserSettingsEndpoints.UserPhone.UserPhone.ActivatePhone.ActivatePhoneDTO;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChangeNumberEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    ValidateAuthKey validateAuthKey;
    @Autowired
    SetNewLog setNewLog;

    @PostMapping("/changePhoneNumber")
    public ResponseEntity<Map<String, Object>> changeNumber(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangeNumberDTO request,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        //System.out.println("numer: " + request.phone + ", code: " +  request.activationKey);
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)) {
                if(request.phone!=null && request.activationKey!=null) {
                    String email = jwtUtil.extractUsername(token);
                    Boolean isValidated = validateAuthKey.validatePhone(request.phone, request.activationKey);
                    if (isValidated) {
                        userService.setUserPhone(email, request.phone);
                        long userId = userService.getUserIdByMail(email);
                        setNewLog.setLog(28, ip, userId, userId);   //Loguj dodanie numeru telefonu
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", true));
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "success", false));
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "empty key","success", false));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid","success", false));
    }
}
