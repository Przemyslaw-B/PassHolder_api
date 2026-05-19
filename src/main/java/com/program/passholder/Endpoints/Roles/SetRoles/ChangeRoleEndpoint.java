package com.program.passholder.Endpoints.Roles.SetRoles;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Roles.RoleEntity;
import com.program.passholder.Database.Querry.Roles.RoleService;
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
public class ChangeRoleEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    GetRoleFromUserMail getRoleFromUserMail;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    GetFromMail getFromMail;

    @PostMapping("/modifyUserRole")
    public ResponseEntity<Map<String, Object>> removeRecordFromStorage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SetRoleDTO requestBody,
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
                long userId = getFromMail.getUserIdFromMail(userMail);
                String userModMail = requestBody.userModMail;
                long userModId = userService.getUserIdByMail(userModMail);

                Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
                Optional<Integer> userModRole =getRoleFromUserMail.getRoleId(userModMail);
                if(userRole.isEmpty() || userModRole.isEmpty() || userRole.get() <= userModRole.get()){
                    setNewLog.setLog(16, ip, userId);
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
                }
                String newRoleName = requestBody.newRoleName;
                if(newRoleName!= null && !newRoleName.isBlank()){
                    Optional <RoleEntity> newRole = roleService.getByName(newRoleName);
                    if(newRole.isPresent()) {
                        int newRoleId = newRole.get().getId();
                        boolean result = userRoleService.changeUserRole(userModId, newRoleId);
                        if(result) {
                            setNewLog.setLog(20, ip, userModId, userId);
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok"));
                        } else{
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "edycja nie powiodła się"));
                        }
                    } else{
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "podana rola nie istnieje"));
                    }
                } else{
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "brak podanej roli"));
                }
            }
            setNewLog.setLog(12, ip);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "zły token"));
        }
        setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
