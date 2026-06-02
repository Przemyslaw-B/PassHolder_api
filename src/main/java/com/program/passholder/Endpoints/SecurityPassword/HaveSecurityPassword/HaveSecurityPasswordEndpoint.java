package com.program.passholder.Endpoints.SecurityPassword.HaveSecurityPassword;

import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
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

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HaveSecurityPasswordEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/haveSecurityPassword")
    public ResponseEntity<Map<String, Object>> haveSecurityPassword(
            @RequestHeader("Authorization") String authHeader){
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(jwtUtil.validateToken(token)) {
                String userMail = jwtUtil.extractUsername(token);
                long userId = getFromMail.getUserIdFromMail(userMail);
                Optional<UserEntity> entity = userService.getEntityByid(userId);
                if(entity.isPresent()) {
                    String secPass = entity.get().getSecurity_password();
                    if(secPass!= null && !secPass.isEmpty()){
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "data", true));
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "data", false));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid", "data", false));
    }
}
