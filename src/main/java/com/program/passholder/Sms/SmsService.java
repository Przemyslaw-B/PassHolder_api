package com.program.passholder.Sms;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SmsService {
    private final WebClient smsApiWebClient;

    @Value("${smsapi.from}")
    private String from;

    public SmsService(WebClient smsApiWebClient) {
        this.smsApiWebClient = smsApiWebClient;
    }

    public void sendSmsAuth(String phone, String code) {
        String message = "Twój kod weryfikacyjny: " + code;
        smsApiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/sms.do")
                        .queryParam("to", phone)
                        .queryParam("message", message)
                        .queryParam("from", from)
                        .queryParam("format", "json")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
