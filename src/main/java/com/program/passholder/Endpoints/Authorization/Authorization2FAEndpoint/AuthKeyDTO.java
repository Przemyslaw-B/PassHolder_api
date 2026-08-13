package com.program.passholder.Endpoints.Authorization.Authorization2FAEndpoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthKeyDTO {
    public String email;
    public String authKey;
}
