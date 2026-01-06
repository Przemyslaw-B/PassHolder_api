package com.program.passholder.Database.Querry.User.Authentication;

import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetAuthKey {
    private final UserService userService;

    @Autowired
    public GetAuthKey(UserService userService) {
        this.userService = userService;
    }

    public String getAuthKey(String email) {
        return userService.getAuthKeyByMail(email);
    }
}
