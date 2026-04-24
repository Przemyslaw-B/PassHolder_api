package com.program.passholder.Endpoints.GetRoles;

import com.program.passholder.Database.Querry.Roles.RoleEntity;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
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

@RestController
@RequestMapping("/api")
public class GetRolesEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/recieveRoles")
    public ResponseEntity<Map<String, Object>> getRoles(
        @RequestHeader("Authorization") String authHeader){
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            System.out.println("API ROLE ENDPOINT!");
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);
            //TODO rolecheck!
            List<UserRoleEntity> roleList = new ArrayList<UserRoleEntity>();
            roleList = userRoleService.findAll();
            System.out.println("Role: "+ roleList.size());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "roles", roleList));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }


}
