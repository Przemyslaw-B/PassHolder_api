package com.program.passholder.Database.Querry.User.Login;

import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetPasswordFromEmail {

    private final UserService userService;

    @Autowired
    public GetPasswordFromEmail(UserService userService) {
        this.userService = userService;
    }

    public String getUserPasswordFromEmail(String email){
        return userService.getPasswordByEmail(email);
    }
}
