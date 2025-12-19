package com.program.passholder.Endpoints.RSA;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class RsaRequest {
    private String email;

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}
