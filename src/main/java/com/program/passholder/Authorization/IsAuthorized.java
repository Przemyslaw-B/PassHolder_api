package com.program.passholder.Authorization;

import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IsAuthorized {
    UserService userService;
    @Autowired
    public IsAuthorized(UserService userService) {
        this.userService = userService;
    }

    public boolean isAuthorized(String email){
        int val = userService.isAuthNeeded(email);
        if(val != 0){
            return true;
        }
        return false;
    }
}
