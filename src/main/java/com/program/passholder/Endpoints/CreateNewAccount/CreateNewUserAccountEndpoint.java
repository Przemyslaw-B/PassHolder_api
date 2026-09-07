package com.program.passholder.Endpoints.CreateNewAccount;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.CreateNewUser;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CreateNewUserAccountEndpoint {
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    CreateNewUser createNewUser;


    @PostMapping("/CreateNewAccount")
    public ResponseEntity<Map<String, String>> createNewAccount(
            @RequestBody NewUserDTO request,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        System.out.println("Otrzymano request utworzenia konta!");
        String email = request.email;
        String password = request.password;
        String name = request.name;
        String salt = request.salt;
        boolean isExist = userService.isUserExist(email);
        //System.out.println("isExist? " + isExist);
        if(email.isEmpty() || password.isEmpty() ||name.isEmpty() ){
            return ResponseEntity.ok(Map.of("status", "emptyForm"));
        }
        if(isExist){
            //setNewLog.setLog(18, ip);
            return ResponseEntity.ok(Map.of("status", "alreadyExist"));
        }
        createNewUser.createNewUser(email, name, password, salt);
        long userId = userService.getUserIdByMail(email);

        setNewLog.setLog(5, ip, userId, userId);
        return ResponseEntity.ok(Map.of("status", "accountCreated"));
    }

}
