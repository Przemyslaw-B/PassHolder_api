package com.program.passholder.Endpoints.Login.LoginCredentialsReceivingEndpoint;

import com.program.passholder.Authorization.IsAuthorized;
import com.program.passholder.Authorization.ProceedAuth;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.*;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.GoogleAuthenticator.TOTPService;
import com.program.passholder.Login.LoginCredentialsProcessing.ValidationUser;
import com.program.passholder.Session.JwtUtil;
import com.program.passholder.Sms.SmsVerifyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    SmsVerifyService smsVerifyService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TOTPService totpService;

    @PostMapping("/userValidation")
public ResponseEntity<Map<String, Object>> isUserValidEndpoint(
        @RequestBody LoginRequest request,
        HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        //System.out.println("Weryfikacja Usera");
        String email = request.getEmail();
        String password = request.getPassword();
        if(!userService.isUserExist(email)){
            return ResponseEntity.ok(Map.of("status", "Invalid"));
        }
        String username = getUserFromMail.getUserFromMail(email);
        long userId = getFromMail.getUserIdFromMail(email);
        String securityPassword = userService.getSecurityPasswordById(userId);
        Optional<UserEntity> userEntity = userService.getEntityByid(userId);
        if (userEntity.isPresent()){
            int userAuthMethode = userEntity.get().getNotificationMethod();
            String token = jwtUtil.generateToken(email);
            boolean authorized = isAuthorized.isAuthorized(email);
            String auth = Boolean.toString(authorized);
            String userPhone = userEntity.get().getPhone();
            String qrCode = "";

            HashMap<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("token", token);
            data.put("securityPassword", securityPassword);
            data.put("auth", auth);
            data.put("status", "Validated");

            proceedAuth.proceed(email); //wyślij wiadomość do usera

            if(userAuthMethode == 3){
                qrCode = totpService.getQrCode(email);
            }
            return ResponseEntity.ok(Map.of("status", "Validated","data", data, "authMethode", userAuthMethode, "qrCode", qrCode));
        }
        setNewLog.setLog(4,ip, userId);
        return ResponseEntity.ok(Map.of("status", "Invalid"));
    }

}
