package com.program.passholder.Database.Querry.User.User;

import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetUserFromMail {
    private final UserService userService;

    @Autowired
    public GetUserFromMail(UserService userService) {
        this.userService = userService;
    }

    public String getUserFromMail(String email){
        return userService.getNameByMail(email);
    }
}
