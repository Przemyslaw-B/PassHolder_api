package com.program.passholder.PasswordRestore;

import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateResetPasswordToken {
    @Autowired
    UserService userService;

    public boolean validatePasswordResetToken(String email, String token) {
        if(email.isEmpty() || token.isEmpty()){return false;}
        String savedToken = userService.getPasswordResetTokenByMail(email);
        if(savedToken.equals(token)){
            return true;
        }
        return false;
    }
}
