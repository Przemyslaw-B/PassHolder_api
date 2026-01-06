package com.program.passholder.Authorization;

import com.program.passholder.Mailings.Requests.AuthenticatorMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendAuthKeyToUser {
    AuthenticatorMail authenticatorMail;
    @Autowired
    public SendAuthKeyToUser(AuthenticatorMail authenticatorMail) {
        this.authenticatorMail = authenticatorMail;
    }

    protected void sendEmail(String email, String key) {
        authenticatorMail.sendAuthenticatorMail(email, key);
    }
}
