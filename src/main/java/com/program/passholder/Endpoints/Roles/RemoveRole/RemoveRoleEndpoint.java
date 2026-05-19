package com.program.passholder.Endpoints.Roles.RemoveRole;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Endpoints.Roles.GetRoleFromUserMail;
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
public class RemoveRoleEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    GetRoleFromUserMail getRoleFromUserMail;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    SetNewLog setNewLog;

    @PostMapping("/removeUserRole")
    public ResponseEntity<Map<String, Object>> removeUserRole(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RemoveRoleDTO requestBody,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(token!= null && jwtUtil.validateToken(token)) {
                String userMail = jwtUtil.extractUsername(token);
                String userModMail = requestBody.userModMail;
                long userId = getFromMail.getUserIdFromMail(userMail);
                long userModId = getFromMail.getUserIdFromMail(userModMail);

                Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
                if(userRole.isEmpty() || userRole.get() < 2){
                    setNewLog.setLog(16, ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
                }

                Optional<Integer> userRoleId =getRoleFromUserMail.getRoleId(userMail);
                Optional<Integer> userModRoleId =getRoleFromUserMail.getRoleId(userModMail);
                if(userRoleId.isPresent() && userModRoleId.isPresent()) {
                    boolean result = userRoleService.changeUserRoleToDefaultUser(userModRoleId.get());
                    if(result) {
                        setNewLog.setLog(19,ip, userModId, userId);
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok"));
                    } else{
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "edycja nie powiodła się"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "nie znaleziono użytkownika"));
                }
            }
            setNewLog.setLog(12,ip);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "zły token"));
        }
        setNewLog.setLog(12,ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
