package com.program.passholder.Endpoints.LoginCredentialsReceivingEndpoint;

import com.program.passholder.Authorization.IsAuthorized;
import com.program.passholder.Authorization.ProceedAuth;
import com.program.passholder.Database.Querry.User.User.*;
import com.program.passholder.Login.LoginCredentialsProcessing.ValidationUser;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/userValidation")
public ResponseEntity<Map<String, String>> isUserValidEndpoint(@RequestBody LoginRequest request){
        //System.out.println("Weryfikacja Usera");
        String email = request.getEmail();
        String password = request.getPassword();
        boolean isValid=false;
        isValid = validationUser.validateUser(email, password);
        System.out.println("Is user: " + email + " Valid?: " + isValid);
        String username = getUserFromMail.getUserFromMail(email);
        System.out.println("Username From mail: " + username);
        if (isValid && username != null){
            String token = jwtUtil.generateToken(email);
            boolean authorized = isAuthorized.isAuthorized(email);
            String auth = Boolean.toString(authorized);
            System.out.println("Is authorized needed?: " + auth);
            if(auth.equals("false")){   // Użytkownik wymaga dodatkowej autoryzacji
                proceedAuth.proceed(email); //Wyślij kod autoryzacyjny na email
            }
            return ResponseEntity.ok(Map.of("status", "Validated","username", username, "token",token, "auth", auth));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
