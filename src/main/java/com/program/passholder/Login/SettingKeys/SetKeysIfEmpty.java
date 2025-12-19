package com.program.passholder.Login.SettingKeys;

import com.program.passholder.Database.Querry.User.PrivateKey.SetPrivateKeyByEmail;
import com.program.passholder.Database.Querry.User.PublicKey.SetPublicKeyByEmail;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Encryption.RsaKey.RsaKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component
public class SetKeysIfEmpty {

    @Autowired
    UserService userService;
    @Autowired
    SetPublicKeyByEmail setPublicKeyByEmail;
    @Autowired
    SetPrivateKeyByEmail setPrivateKeyByEmail;
    @Autowired
    RsaKeyGenerator rsaKeyGenerator;

    public boolean setKeysIfEmpty(String email){
        String publicKey = userService.getPublicKeyByEmail(email);
        String privateKey = userService.getPrivateKeyByEmail(email);
        if(publicKey == null && privateKey == null){
            try {
                Map<String, String> map = rsaKeyGenerator.generateRsaKey();
                publicKey = map.getOrDefault("publicKey", null);
                privateKey = map.getOrDefault("privateKey", null);
                setPublicKeyByEmail.setPublicKeyByEmail(email, publicKey);
                setPrivateKeyByEmail.setPrivateKeyByEmail(email, privateKey);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }
}
