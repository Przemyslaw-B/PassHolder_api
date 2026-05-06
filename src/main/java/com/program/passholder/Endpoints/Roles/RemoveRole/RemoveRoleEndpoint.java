package com.program.passholder.Endpoints.Roles.RemoveRole;

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
public class RemoveRoleEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    GetRoleFromUserMail getRoleFromUserMail;

    @PostMapping("/removeUserRole")
    public ResponseEntity<Map<String, Object>> removeUserRole(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RemoveRoleDTO requestBody) {
        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(token!= null && jwtUtil.validateToken(token)) {
                String userMail = jwtUtil.extractUsername(token);
                String userModMail = requestBody.userModMail;

                Optional<Integer> userRoleId =getRoleFromUserMail.getRoleId(userMail);
                Optional<Integer> userModRoleId =getRoleFromUserMail.getRoleId(userModMail);
                if(userRoleId.isPresent() && userModRoleId.isPresent()) {
                    if(userRoleId.get() > userModRoleId.get()) {
                        boolean result = userRoleService.changeUserRoleToDefaultUser(userModRoleId.get());
                        if(result) {
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok"));
                        } else{
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "edycja nie powiodła się"));
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "brak uprawnień"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "nie znaleziono użytkownika"));
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "zły token"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
