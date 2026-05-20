package com.program.passholder.Sms;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsVerifyService {

    private final SmsProperties properties;

    @Autowired
    SmsVerifyService(SmsProperties properties) {
        this.properties = properties;
    }

    public void sendVerificationCode(String phoneNumber) {

        Verification.creator(
                properties.getVerifyServiceSid(),
                phoneNumber,
                "sms"
        ).create();
    }

    public boolean verifyCode(String phoneNumber, String code) {

        VerificationCheck verificationCheck =
                VerificationCheck.creator(properties.getVerifyServiceSid())
                        .setTo(phoneNumber)
                        .setCode(code)
                        .create();

        return "approved".equals(verificationCheck.getStatus());
    }
}
