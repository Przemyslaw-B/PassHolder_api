package com.program.passholder.Endpoints.UserSettingsEndpoints.UserPhone.UserPhone;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Endpoints.RSA.RsaRequest;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GetUserPhoneEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    private GetFromMail getFromMail;

    @GetMapping("/getUserPhone")
    public ResponseEntity<Map<String, Object>> getKey(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (token != null && jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractUsername(token);
                long userId = getFromMail.getUserIdFromMail(email);

                Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
                if(userRole.isEmpty() || userRole.get() < 1){
                    setNewLog.setLog(16, ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
                }

                Optional<UserEntity> entity = userService.getEntityByMail(email);
                HashMap<String, String> map = new HashMap<>();
                if(entity.isPresent()){
                    String phone = entity.get().getPhone();
                    map.put("phone", phone);
                    setNewLog.setLog(27, ip, userId);   //loguj wysłanie kodu aktywacyjnego sms
                    return ResponseEntity.ok(Map.of("status", "ok", "data", map));
                } else{
                    return ResponseEntity.ok(Map.of("status", "ok", "error", "user not found"));
                }
            }
        }
        //setNewLog.setLog(12, ip);
        return ResponseEntity.ok(Map.of("status", "Invalid", "publicKey", ""));
    }
}
