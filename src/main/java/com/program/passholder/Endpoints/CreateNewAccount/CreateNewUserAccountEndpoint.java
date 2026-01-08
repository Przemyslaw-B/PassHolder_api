package com.program.passholder.Endpoints.CreateNewAccount;

import com.program.passholder.Database.Querry.User.User.CreateNewUser;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Endpoints.LoginCredentialsReceivingEndpoint.LoginRequest;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CreateNewUserAccountEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    @Autowired
    CreateNewUser createNewUser;


    @PostMapping("/CreateNewAccount")
    public ResponseEntity<Map<String, String>> createNewAccount(@RequestBody NewUserDTO request){
        System.out.println("Otrzymano request utworzenia konta!");
        String email = request.email;
        String password = request.password;
        String name = request.name;
        boolean isExist = userService.isUserExist(email);
        System.out.println("isExist? " + isExist);
        if(email.isEmpty() || password.isEmpty() ||name.isEmpty() ){
            return ResponseEntity.ok(Map.of("status", "emptyForm"));
        }
        if(isExist){
            return ResponseEntity.ok(Map.of("status", "alreadyExist"));
        }
        createNewUser.createNewUser(email, name, password);
        return ResponseEntity.ok(Map.of("status", "accountCreated"));
    }

}
