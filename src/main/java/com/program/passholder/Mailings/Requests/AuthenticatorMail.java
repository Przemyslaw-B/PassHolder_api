package com.program.passholder.Mailings.Requests;

import com.program.passholder.Mailings.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatorMail {
    EmailService emailService;

    @Autowired
    public AuthenticatorMail(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendAuthenticatorMail(String email, String key){
        String topic = "PassHolder - kod uwierzytelniający";
        String text = "Oto Twój kod: " + key;
        emailService.sendEmail(email, topic, text);
    }

}
