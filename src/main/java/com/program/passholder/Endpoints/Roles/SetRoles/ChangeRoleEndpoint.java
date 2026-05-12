package com.program.passholder.Endpoints.Roles.SetRoles;

import com.program.passholder.Database.Querry.Roles.RoleEntity;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Endpoints.Roles.GetRoleFromUserMail;
import com.program.passholder.Session.JwtUtil;
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

    @PostMapping("/modifyUserRole")
    public ResponseEntity<Map<String, Object>> removeRecordFromStorage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SetRoleDTO requestBody) {
        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(token!= null && jwtUtil.validateToken(token)) {
                String userMail = jwtUtil.extractUsername(token);
                String userModMail = requestBody.userModMail;
                long userModId = userService.getUserIdByMail(userModMail);
                Optional<Integer> userRoleId = getRoleFromUserMail.getRoleId(userMail);
                Optional<Integer> userModRoleId =getRoleFromUserMail.getRoleId(userModMail);
                if(userRoleId.isPresent() && userModRoleId.isPresent()) {
                    if(userRoleId.get() > userModRoleId.get()) {
                        String newRoleName = requestBody.newRoleName;
                        if(newRoleName!= null && !newRoleName.isBlank()){
                            Optional <RoleEntity> newRole = roleService.getByName(newRoleName);
                            if(newRole.isPresent()) {
                                int newRoleId = newRole.get().getId();
                                boolean result = userRoleService.changeUserRole(userModId, newRoleId);
                                if(result) {
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
                    } else{
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "brak uprawnień"));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "zły token"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
