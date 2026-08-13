package com.program.passholder.Endpoints.Roles.GetRoles;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Roles.RoleEntity;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Roles.GetRoleDetailsOfUsers;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GetRolesEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    GetRoleDetailsOfUsers getRoleDetailsOfUsers;
    @Autowired
    SetNewLog setNewLog;

    @GetMapping("/recieveRoles")
    public ResponseEntity<Map<String, Object>> getRoles(
        @RequestHeader("Authorization") String authHeader,
        HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            //System.out.println("API ROLE ENDPOINT!");
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);

            Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
            if(userRole.isEmpty() || userRole.get() < 3){
                setNewLog.setLog(16, ip, userId);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
            }

            List<UserRoleEntity> roleList = new ArrayList<UserRoleEntity>();
            //roleList = userRoleService.findAll();
            roleList = userRoleService.findAllAdmins();
            //System.out.println("Role: "+ roleList.size());
            List<Object> list = getRoleDetailsOfUsers.getDetails(roleList);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "roles", list));
        }
        //setNewLog.setLog(12,ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }


}
