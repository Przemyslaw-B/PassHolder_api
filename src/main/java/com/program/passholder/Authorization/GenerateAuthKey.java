package com.program.passholder.Authorization;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

public class GenerateAuthKey {
    protected String generateKey(){
        SecureRandom random = new SecureRandom();
        String code = String.format("%06d", random.nextInt(1_000_000));
        return code;
    }
}
