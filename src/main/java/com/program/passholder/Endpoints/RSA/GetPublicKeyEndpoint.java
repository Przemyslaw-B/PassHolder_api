package com.program.passholder.Endpoints.RSA;

import com.program.passholder.Database.Querry.User.PublicKey.GetPublicKeyByEmail;
import com.program.passholder.Endpoints.LoginCredentialsReceivingEndpoint.LoginRequest;
import com.program.passholder.Login.SettingKeys.SetKeysIfEmpty;
import com.program.passholder.Session.JwtUtil;
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
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("GetPublicKey")
    public ResponseEntity<Map<String, String>> getKey(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RsaRequest rsaRequest) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (token != null && jwtUtil.validateToken(token)) {
                //String email = rsaRequest.getEmail();
                String email = jwtUtil.extractUsername(token);
                setKeysIfEmpty.setKeysIfEmpty(email);
                String publicKey = getPublicKeyByEmail.getPublicKeyByEmail(email);
                if (publicKey == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Public key not found.", "publicKey", ""));
                }
                return ResponseEntity.ok(Map.of("status", "Ok", "publicKey", publicKey));
            }
        }
        return ResponseEntity.ok(Map.of("status", "Invalid", "publicKey", ""));
    }


}
