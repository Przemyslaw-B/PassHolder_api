package com.program.passholder.Encryption.RsaKey;

import org.springframework.stereotype.Component;

import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Component
public class RsaKeyGenerator {
    Map<String, String> map = new HashMap<String, String>();


    public Map<String, String> generateRsaKey() throws NoSuchAlgorithmException {
        //Generowanie kluczy
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  //długość klucza [bit]
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        //Zmiana na Base64
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        //System.out.println("RSA Key public: " + publicKeyBase64);
        //System.out.println("RSA Key private: " + privateKeyBase64);
        map.put("publicKey", publicKeyBase64);
        map.put("privateKey", privateKeyBase64);
        return map;
    }
}
