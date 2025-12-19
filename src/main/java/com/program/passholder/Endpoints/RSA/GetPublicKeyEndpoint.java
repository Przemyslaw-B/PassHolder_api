package com.program.passholder.Endpoints.RSA;

import com.program.passholder.Database.Querry.User.PublicKey.GetPublicKeyByEmail;
import com.program.passholder.Endpoints.LoginCredentialsReceivingEndpoint.LoginRequest;
import com.program.passholder.Login.SettingKeys.SetKeysIfEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GetPublicKeyEndpoint {

    @Autowired
    GetPublicKeyByEmail getPublicKeyByEmail;
    @Autowired
    SetKeysIfEmpty setKeysIfEmpty;

    @PostMapping("GetPublicKey")
    public ResponseEntity<Map<String, String>> getKey(@RequestBody RsaRequest rsaRequest){
        //System.out.println("~~~ Get Public Key ~~~");
        String email = rsaRequest.getEmail();
        setKeysIfEmpty.setKeysIfEmpty(email);
        //System.out.println("Podany Email: " + email);
        String publicKey = getPublicKeyByEmail.getPublicKeyByEmail(email);
        //System.out.println("Public key: " + publicKey);

        if(publicKey == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Public key not found."));
        }
        return ResponseEntity.ok(Map.of("publicKey", publicKey));
    }
}
