package com.program.passholder.Sms;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    private final SmsProperties properties;

    public SmsConfig(SmsProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        Twilio.init(
                properties.getAccountSid(),
                properties.getAuthToken()
        );
    }
}