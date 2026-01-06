package com.program.passholder.Authorization;

import com.program.passholder.Database.Querry.User.Authentication.SetIsAuthorizatedStatus;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateAuthKey {
    UserService userService;
    SetIsAuthorizatedStatus setIsAuthorizatedStatus;


    @Autowired
    ValidateAuthKey(UserService userService, SetIsAuthorizatedStatus setIsAuthorizatedStatus){
        this.userService = userService;
        this.setIsAuthorizatedStatus = setIsAuthorizatedStatus;
    }

    public boolean validateAuthKey(String email, String userAuthKey){
        if(email != null && userAuthKey != null){
            String databaseAuthKey = userService.getAuthKeyByMail(email);
            if(userAuthKey.equals(databaseAuthKey)){
                setIsAuthorizatedStatus.setAuthStatus(email, 1);    //1 jako true
                return true;
            }
        }
        return false;
    }
}
