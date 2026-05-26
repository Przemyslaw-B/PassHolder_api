package com.program.passholder.Authorization;

import com.program.passholder.Database.Querry.User.Authentication.SetIsAuthorizatedStatus;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.GoogleAuthenticator.TOTPService;
import com.program.passholder.Sms.SmsVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateAuthKey {
    UserService userService;
    SetIsAuthorizatedStatus setIsAuthorizatedStatus;
    SmsVerifyService smsVerifyService;
    TOTPService totpService;

    @Autowired
    ValidateAuthKey(UserService userService, SetIsAuthorizatedStatus setIsAuthorizatedStatus, SmsVerifyService smsVerifyService, TOTPService totpService) {
        this.userService = userService;
        this.setIsAuthorizatedStatus = setIsAuthorizatedStatus;
        this.smsVerifyService = smsVerifyService;
        this.totpService = totpService;
    }

    public boolean validate(String email, String userAuthKey){
        boolean isValidated = false;
        Optional<UserEntity> userEntity = userService.getEntityByMail(email);
        if(userEntity.isPresent()){
            long userId = userEntity.get().getId();
            int authMethode = userEntity.get().getNotificationMethod();
            switch (authMethode){
                case 1:
                    isValidated = validateEmail(email,  userAuthKey);
                    break;
                case 2:
                    String userPhone = userEntity.get().getPhone();
                    isValidated=validatePhone(userPhone,  userAuthKey);
                    break;
                case 3:
                    isValidated = validateTOTP(email,  userAuthKey);
                    break;
            }
        }
        return isValidated;
    }

    public boolean validateMethode(String email, int methodeId, String userAuthKey){
        boolean isValidated = false;
        Optional<UserEntity> userEntity = userService.getEntityByMail(email);
        if(userEntity.isPresent()) {
            switch (methodeId) {
                case 1:
                    isValidated = validateEmail(email, userAuthKey);
                    break;
                case 2:
                    String userPhone = userEntity.get().getPhone();
                    isValidated = validatePhone(userPhone, userAuthKey);
                    break;
                case 3:
                    isValidated = validateTOTP(email, userAuthKey);
                    break;

            }
        }
        return isValidated;
    }

    private boolean validateEmail(String email, String userAuthKey){
        if(email != null && userAuthKey != null){
            String databaseAuthKey = userService.getAuthKeyByMail(email);
            if(userAuthKey.equals(databaseAuthKey)){
                setIsAuthorizatedStatus.setAuthStatus(email, 1);    //1 jako true
                return true;
            }
        }
        return false;
    }

    public boolean validatePhone(String userPhone, String userAuthKey){
        boolean validated = false;
        if(userPhone != null && userAuthKey != null){
            validated = smsVerifyService.verifyCode(userPhone, userAuthKey);
        }
        return validated;
    }

    private boolean validateTOTP(String email, String userAuthKey){
        if(email == null && userAuthKey == null){ return false;}
        if(totpService.verifyCode(email, userAuthKey)){
            return true;
        }
        return false;
    }


}
