package com.program.passholder.Authenticator;

import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import org.springframework.stereotype.Component;

@Component
public class TOTPSecretGenerator {
    private final SecretGenerator generator = new DefaultSecretGenerator();

    public String generateSecret(){
        return generator.generate();
    }
}
