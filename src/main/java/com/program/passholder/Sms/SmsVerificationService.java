package com.program.passholder.Sms;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsVerificationService {
    @Value("${twilio.verify-service-sid}")
    private String verifyServiceSid;

    public void sendVerificationCode(String phoneNumber) {

        Verification.creator(
                verifyServiceSid,
                phoneNumber,
                "sms"
        ).create();
    }

    public boolean verifyCode(String phoneNumber, String code) {

        VerificationCheck verificationCheck =
                VerificationCheck.creator(verifyServiceSid)
                        .setTo(phoneNumber)
                        .setCode(code)
                        .create();

        return "approved".equals(verificationCheck.getStatus());
    }
}

