package com.program.passholder.PasswordRestore;

import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RemovePasswordResetToken {
    @Autowired
    UserService userService;

    public void removePasswordResetToken(String email) {
        if(!email.isEmpty()){
            userService.removePasswordResetToken(email);
        }
    }
}
