package com.program.passholder.Authorization;

import com.program.passholder.Database.Querry.User.Authentication.SetAuthKey;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.GoogleAuthenticator.TOTPService;
import com.program.passholder.Sms.SmsVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProceedAuth {
SendAuthKeyToUser sendAuthKeyToUser;
SetAuthKey setAuthKey;
SmsVerifyService smsVerifyService;
UserService userService;
TOTPService totpService;

@Autowired
public ProceedAuth(SendAuthKeyToUser sendAuthKeyToUser, SetAuthKey setAuthKey, SmsVerifyService smsVerifyService, UserService userService, TOTPService totpService) {
    this.sendAuthKeyToUser = sendAuthKeyToUser;
    this.setAuthKey = setAuthKey;
    this.smsVerifyService = smsVerifyService;
    this.userService = userService;
    this.totpService = totpService;
}

    @Async
    public void proceed(String email){
        Optional<UserEntity> userEntity = userService.getEntityByMail(email);
        if(userEntity.isPresent()){
            int authMehode = userEntity.get().getNotificationMethod();

            switch(authMehode) {
                case 2: //SMS
                    String userPhone = userEntity.get().getPhone();
                    smsVerifyService.sendVerificationCode(userPhone);
                    break;
                case 3: //GOOGLE AUTH
                    if(userEntity.get().getTotpSecret()==null){
                        totpService.setSecret(email);
                    }
                    break;
                default:    //EMAIL by default
                    GenerateAuthKey generateAuthKey = new GenerateAuthKey();
                    String generatedKey = generateAuthKey.generateKey();
                    setAuthKey.setAuthKey(email, generatedKey);
                    sendAuthKey(email, generatedKey);
                    break;
            }
        }
    }

    @Async
    public void sendKeyToPickedMethode(String email, int methodeId){
        Optional<UserEntity> userEntity = userService.getEntityByMail(email);
        if(userEntity.isPresent()){
            switch(methodeId) {
                case 2: //SMS
                    String userPhone = userEntity.get().getPhone();
                    smsVerifyService.sendVerificationCode(userPhone);
                    break;
                case 3: //GOOGLE AUTH
                    if(userEntity.get().getTotpSecret()==null){
                        totpService.setSecret(email);
                    }
                    break;
                default:    //default EMAIL
                    GenerateAuthKey generateAuthKey = new GenerateAuthKey();
                    String generatedKey = generateAuthKey.generateKey();
                    setAuthKey.setAuthKey(email, generatedKey);
                    //sendAuthKey(email, generatedKey);
                    sendAuthKeyToUser.sendEmail(email, generatedKey);
                    break;
            }
        }
    }

    @Async
    void sendAuthKeyEmail(String email, String key){
        sendAuthKeyToUser.sendEmail(email, key);       //wysłanie email
    }

    @Async
    void sendAuthKey(String email, String key){
        sendAuthKeyToUser.send(email, key);
    }

    @Async
    public void sendAuthKeySms(String phone){
        smsVerifyService.sendVerificationCode(phone);
    }

}
