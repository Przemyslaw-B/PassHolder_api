package com.program.passholder.Endpoints.LoginCredentialsReceivingEndpoint;

import com.program.passholder.Authorization.IsAuthorized;
import com.program.passholder.Authorization.ProceedAuth;
import com.program.passholder.Database.Querry.User.User.*;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Login.LoginCredentialsProcessing.ValidationUser;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginValidationEndpoint {
    @Autowired
    ValidationUser validationUser;
    @Autowired
    GetUserFromMail getUserFromMail;
    @Autowired
    GetUserTokenFromMail getUserTokenFromMail;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    ProceedAuth proceedAuth;
    @Autowired
    IsAuthorized isAuthorized;
    @Autowired
    UserService userService;

    @PostMapping("/userValidation")
public ResponseEntity<Map<String, Object>> isUserValidEndpoint(@RequestBody LoginRequest request){
        //System.out.println("Weryfikacja Usera");
        String email = request.getEmail();
        String password = request.getPassword();
        if(!userService.isUserExist(email)){
            return ResponseEntity.ok(Map.of("status", "Invalid"));
        }
        boolean isValid=false;
        isValid = validationUser.validateUser(email, password);
        String username = getUserFromMail.getUserFromMail(email);
        long userId = userService.getUserIdByMail(email);
        String securityPassword = userService.getSecurityPasswordById(userId);
        if (isValid && username != null){
            String token = jwtUtil.generateToken(email);
            boolean authorized = isAuthorized.isAuthorized(email);
            String auth = Boolean.toString(authorized);
            System.out.println("Username From mail: " + username);
            System.out.println("Is authorized needed?: " + auth);
            HashMap<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("token", token);
            data.put("securityPassword", securityPassword);
            data.put("auth", auth);
            data.put("status", "Validated");
            if(auth.equals("false")){   // Użytkownik wymaga dodatkowej autoryzacji
                proceedAuth.proceed(email); //Wyślij kod autoryzacyjny na email
            }
            //return ResponseEntity.ok(Map.of("status", "Validated","username", username, "token",token, "auth", auth));
            return ResponseEntity.ok(Map.of("status", "Validated","data", data));
        }
        return ResponseEntity.ok(Map.of("status", "Invalid"));
    }

}
