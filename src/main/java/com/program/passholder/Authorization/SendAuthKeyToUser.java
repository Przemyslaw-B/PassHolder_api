package com.program.passholder.Authorization;

import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Mailings.Requests.AuthenticatorMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SendAuthKeyToUser {
    AuthenticatorMail authenticatorMail;
    UserService userService;
    @Autowired
    public SendAuthKeyToUser(AuthenticatorMail authenticatorMail, UserService userService) {
        this.authenticatorMail = authenticatorMail;
        this.userService = userService;
    }

    public void send(String email, String key){
        Optional<UserEntity> entity =  userService.getEntityByMail(email);
        if(entity.isPresent()){
            int validationMethode = entity.get().getNotificationMethod();
            switch (validationMethode){
                case 1:
                //email
                    authenticatorMail.sendAuthenticatorMail(email, key);
                    break;

                case 2:
                //sms

                    break;

                default:
                    //email by default
                    authenticatorMail.sendAuthenticatorMail(email, key);
                    break;
            }
        }

    }

    public void sendEmail(String email, String key) {
        authenticatorMail.sendAuthenticatorMail(email, key);
    }
}
