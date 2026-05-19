package com.program.passholder.Endpoints.SecurityPassword.GetSecurityPassword;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GetSecurityPasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;

    @GetMapping("/getSecurityPassword")
    public ResponseEntity<Map<String, Object>> getSecurityPassword(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            //System.out.println("Użytkownik chce pobrać swoje hasło bezpieczeństwa..");
            String token = authHeader.substring(7);
            if(jwtUtil.validateToken(token)) {
                String userMail = jwtUtil.extractUsername(token);
                long userId = getFromMail.getUserIdFromMail(userMail);

                Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
                if(userRole.isEmpty() || userRole.get() < 1){
                    setNewLog.setLog(16, ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
                }

                String password = userService.getSecurityPasswordById(userId);
                Map<String, String> map = new HashMap<>();
                map.put("securityPassword", password);
                setNewLog.setLog(21, ip, userId);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "map", map));
            }
            setNewLog.setLog(12, ip);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "token invalid"));
        }
        setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
