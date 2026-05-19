package com.program.passholder.Endpoints.SecurityPassword.SetSecurityPassword;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SetSecurityPasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;

    @PostMapping("/setSecurityPassword")
    public ResponseEntity<Map<String, Object>> setSecurityPasswordEndpoint(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SecurityPasswordDTO request,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            if(token!=null && jwtUtil.validateToken(token)){
                String userEmail = jwtUtil.extractUsername(token);
                long userId = userService.getUserIdByMail(userEmail);

                Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
                if(userRole.isEmpty() || userRole.get() < 1){
                    setNewLog.setLog(16, ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
                }

                String newSecurityPassword = request.newSecurityPassword;
                String userSecurityPassword = userService.getSecurityPasswordById(userId);
                //System.out.println("ustawiam nowe hasło bezpieczeństwa..");
                //System.out.println("request:" + request);
                //System.out.println("newSecurityPassword:" + newSecurityPassword);
                //System.out.println("oldSecurityPassword:" + userSecurityPassword);
                if(userSecurityPassword != null){
                    //System.out.println("Użytkownik posiada już hasło bezpieczeństwa.");
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "securityPassword already set"));
                }
                if(newSecurityPassword != null && !newSecurityPassword.equals("")) {
                    //System.out.println("Ustawiam nowe hasło..");
                    userService.setSecurityPassword(userId, newSecurityPassword);
                    setNewLog.setLog(22,ip,userId,userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok"));

                } else{
                    //System.out.println("Nie odebrano nowego hasło..");
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "new security password is required"));
                }
            } else{
                setNewLog.setLog(12, ip);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "Invalid token"));
            }
        }
        setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
