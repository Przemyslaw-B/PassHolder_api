package com.program.passholder.Endpoints.Authorization.GetGoogleAuthenticatorQrCode;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Authenticator.TOTPService;
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
public class GetQrCodeEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    TOTPService totpService;
    @Autowired
    UserService userService;

    @GetMapping("/getQrCode")
    public ResponseEntity<Map<String, Object>> getQrCodeEndpoin(
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
                String qrCode = "";
                if(userEntity.get().getTotpSecret()==null) {
                    totpService.setSecret(userMail);
                } else {
                    qrCode = totpService.getQrCode(userMail);
                }
                setNewLog.setLog(4,ip, userId, userId); //Log wygenerowania kodu QR
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "qrCode", qrCode));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
