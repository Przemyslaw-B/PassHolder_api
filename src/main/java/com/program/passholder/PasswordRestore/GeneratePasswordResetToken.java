package com.program.passholder.PasswordRestore;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GeneratePasswordResetToken {

    public String generatePasswordResetToken() {
        SecureRandom random = new SecureRandom();
        String code = String.format("%09d", random.nextInt(1_000_000_000));
        return code;
    }
}
