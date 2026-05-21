package com.program.passholder.GoogleAuthenticator;

import com.program.passholder.Authorization.ProceedAuth;
import com.program.passholder.Database.Querry.User.Authentication.SetAuthKey;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.time.SystemTimeProvider;

import java.util.Optional;

@Service
public class TOTPService {
    UserService userService;
    TOTPSecretGenerator secretGenerator;
    SetAuthKey setAuthKey;

    @Autowired
    public TOTPService(UserService userService, TOTPSecretGenerator secretGenerator, SetAuthKey setAuthKey) {
        this.userService = userService;
        this.secretGenerator = secretGenerator;
        this.setAuthKey = setAuthKey;
    }

    public void setSecret(String email){
        String secret = secretGenerator.generateSecret();
        setAuthKey.setTotpSecret(email, secret);

    }

    public String getQrCode(String email){
        Optional<UserEntity> userEntity = userService.getEntityByMail(email);
        if(userEntity.isEmpty()){return null;}
        String secret = userEntity.get().getTotpSecret();
        return String.format(
                "otpauth://totp/PassHolder:%s?secret=%s&issuer=PassHolder",
                email,
                secret
        );
    }

    public boolean verifyCode(String email, String code) {
        Optional<UserEntity> userEntity = userService.getEntityByMail(email);
        if (userEntity.isEmpty()) {
            return false;
        }
        String secret = userEntity.get().getTotpSecret();
        if (secret == null) {
            return false;
        }
        try {
            CodeGenerator codeGenerator = new DefaultCodeGenerator();
            TimeProvider timeProvider = new SystemTimeProvider();
            DefaultCodeVerifier verifier = new DefaultCodeVerifier(
                    codeGenerator,
                    timeProvider
            );
            verifier.setAllowedTimePeriodDiscrepancy(1);
            long time = timeProvider.getTime();
            String generatedCode = codeGenerator.generate(secret, time);
            System.out.println("SECRET: " + secret);
            System.out.println("USER CODE: " + code);
            System.out.println("GENERATED CODE: " + generatedCode);
            System.out.println("TIME: " + time);
            System.out.println("Secret: " + secret + ", code: " + code);
            System.out.println("Verification: " + verifier.isValidCode(secret, code));
            return verifier.isValidCode(secret, code);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
