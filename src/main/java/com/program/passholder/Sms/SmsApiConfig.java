package com.program.passholder.Sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SmsApiConfig {

    @Bean
    public WebClient smsApiWebClient(
            @Value("${smsapi.token}") String token
    ) {

        return WebClient.builder()
                .baseUrl("https://api.smsapi.pl")
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + token
                )
                .build();
    }
}
