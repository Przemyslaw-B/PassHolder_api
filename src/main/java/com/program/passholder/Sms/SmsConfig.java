package com.program.passholder.Sms;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SmsConfig {
    private final SmsProperties properties;

    @PostConstruct
    public void init(){
        Twilio.init(
                properties.getAccountSid(),
                properties.getAuthToken()
        );
    }
}

