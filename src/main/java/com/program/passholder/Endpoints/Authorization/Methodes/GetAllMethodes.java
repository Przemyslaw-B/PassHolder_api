package com.program.passholder.Endpoints.Authorization.Methodes;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesEntity;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesRepository;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Roles.GetRoleDetailsOfUsers;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
public class GetAllMethodes {
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    AuthenticationMethodesService authenticationMethodesService;

    @GetMapping("/getAllAuthenticationMethodes")

    public ResponseEntity<Map<String, Object>> getAllMethodes(
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
            //TODO rolecheck!
            //List<UserRoleEntity> roleList = new ArrayList<UserRoleEntity>();
            //roleList = userRoleService.findAll();
            //System.out.println("Role: "+ roleList.size());
            List<AuthenticationMethodesEntity> tempList = authenticationMethodesService.getAllAuthenticationMethodes();
            List<String> list = new ArrayList<>();
            if(!tempList.isEmpty()){
                for(AuthenticationMethodesEntity temp : tempList){
                    list.add(temp.getName());
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "methodes", list));
        }
        setNewLog.setLog(12,ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
