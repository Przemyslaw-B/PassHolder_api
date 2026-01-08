package com.program.passholder.Authorization;

import com.program.passholder.Database.Querry.User.Authentication.SetAuthKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProceedAuth {
SendAuthKeyToUser sendAuthKeyToUser;
SetAuthKey setAuthKey;

@Autowired
public ProceedAuth(SendAuthKeyToUser sendAuthKeyToUser, SetAuthKey setAuthKey) {
    this.sendAuthKeyToUser = sendAuthKeyToUser;
    this.setAuthKey = setAuthKey;
}

    @Async
    public void proceed(String email){
        GenerateAuthKey generateAuthKey = new GenerateAuthKey();
        String generatedKey = generateAuthKey.generateKey();    //generowanie klucza
        setAuthKey.setAuthKey(email, generatedKey);             //Zapis wygenerowanego klucza
        sendAuthKeyEmail(email, generatedKey);
    }

    @Async
    void sendAuthKeyEmail(String email, String key){
        sendAuthKeyToUser.sendEmail(email, key);       //wysłanie email
    }
}
