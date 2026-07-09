package com.program.passholder.Mailings.Requests;

import com.program.passholder.Mailings.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetToken {
    EmailService emailService;

    @Autowired
    public PasswordResetToken(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendPasswordResetToken(String email, String token) {
        String topic = "Your password reset token is here!";
        String text = "Your token for password reset: " + token;
        emailService.sendEmail(email, topic, text);
    }
}
