package com.program.passholder.Endpoints.Authorization.SetAuthMethode.ActivateNewMethode;

import com.program.passholder.Authentication.ValidateAuthKey;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesEntity;
import com.program.passholder.Database.Querry.AuthenticationMethodes.AuthenticationMethodesService;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ActivateNewMethode {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationMethodesService authenticationMethodesService;
    @Autowired
    ValidateAuthKey validateAuthKey;

    @PostMapping("/activateAuthMethode")
    public ResponseEntity<Map<String, Object>> activateNewMethode(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ActivateNewMethodeDTO request,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")) {
            System.out.println("ENTER ENDPOINT - activate new methode");
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            Optional<UserEntity> entity = userService.getEntityByMail(userMail);
            List<AuthenticationMethodesEntity> methodes = authenticationMethodesService.getAllAuthenticationMethodes();
            if(entity.isPresent() && methodes !=null && !methodes.isEmpty()) {
                long userId = entity.get().getId();
                for(AuthenticationMethodesEntity methode : methodes) {
                    System.out.println("sprawdzam czy to metoda? DB: " + methode.getName() + " === user: " + request.methode);
                    if(request.methode.equals(methode.getName())){
                        int methodeId = methode.getId();
                        Boolean isValidated = validateAuthKey.validateMethode(userMail,methodeId, request.code);

                        //System.out.println("newMethode: " + request.methode);
                        //System.out.println("userCode: " + request.code);
                        //System.out.println("Code in DB: " + entity.get().getAuthKey());
                        //System.out.println("isValidated: " + isValidated);
                        //System.out.println("MethodeId: " + methodeId);
                        if(isValidated) {
                            userService.setAuthMethode(userId, methodeId);
                            if(methodeId == 1){
                                setNewLog.setLog(19, ip, userId, userId);   //LOG zmiany metody autoryzacji na email
                            } else if(methodeId == 2){
                                setNewLog.setLog(20, ip, userId, userId);   //LOG zmiany metody autoryzacji na sms
                            } else if(methodeId == 3){
                                setNewLog.setLog(21, ip, userId, userId);   //LOG zmiany metody autoryzacji na TOTP
                            }
                            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK","success", true));
                        }
                        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK","success", false, "error", "bad code"));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK","success", false, "error", "data not found"));
        }
        setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "auth", "failed"));
    }
}
