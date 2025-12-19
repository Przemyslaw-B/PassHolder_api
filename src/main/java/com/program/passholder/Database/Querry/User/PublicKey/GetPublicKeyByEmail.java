package com.program.passholder.Database.Querry.User.PublicKey;

import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetPublicKeyByEmail {
    private final UserService userService;

    @Autowired
    public GetPublicKeyByEmail(UserService userService) {
        this.userService = userService;
    }

    public String getPublicKeyByEmail(String email){
        return userService.getPublicKeyByEmail(email);
    }
}
