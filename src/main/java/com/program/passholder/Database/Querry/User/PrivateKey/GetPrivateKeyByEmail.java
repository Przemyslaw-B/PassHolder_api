package com.program.passholder.Database.Querry.User.PrivateKey;

import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetPrivateKeyByEmail {
    private final UserService userService;

    @Autowired
    public GetPrivateKeyByEmail(UserService userService) {
        this.userService = userService;
    }

    public String getPrivateKeyByEmail(String email){
        return userService.getPrivateKeyByEmail(email);
    }
}
