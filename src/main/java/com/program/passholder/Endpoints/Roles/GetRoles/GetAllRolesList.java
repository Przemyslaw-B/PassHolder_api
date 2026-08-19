package com.program.passholder.Endpoints.Roles.GetRoles;

import com.program.passholder.Database.Querry.Roles.RoleEntity;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
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
public class GetAllRolesList {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    RoleService roleService;

    @GetMapping("/recieveAllRoles")
    public ResponseEntity<Map<String, Object>> getAllRolesList(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //System.out.println("API ROLE ENDPOINT!");
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);
            List<String> roleList = new ArrayList<String>();
            List<RoleEntity> roleEntityList = roleService.getAll();
            for (RoleEntity roleEntity : roleEntityList) {
                String tempName = roleEntity.getName();
                //System.out.println("Pobieram nazwę:" + tempName);
                if(!tempName.equals("master") && !tempName.equals("user")){
                    roleList.add(tempName);
                    //System.out.println("Nazwa dodana do listy");
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "roles", roleList));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
