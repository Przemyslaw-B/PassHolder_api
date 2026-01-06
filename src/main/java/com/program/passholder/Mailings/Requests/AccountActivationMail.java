package com.program.passholder.Mailings.Requests;

import com.program.passholder.Mailings.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountActivationMail {
    EmailService emailService;

    @Autowired
    public AccountActivationMail(EmailService emailService) {
        this.emailService = emailService;
    }

    public void AccountActivationMail(String email, String url){
        String topic = "Passholder, please confirm Your email.";
        String text = "Please use this link to activate Your account: " + url;
        emailService.sendEmail(email, topic, text);
    }
}
