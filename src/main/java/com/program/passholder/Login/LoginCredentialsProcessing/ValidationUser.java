package com.program.passholder.Login.LoginCredentialsProcessing;

import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Encryption.RsaKey.Decoder;
import com.program.passholder.Login.SettingKeys.SetKeysIfEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ValidationUser {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    public boolean validateUser(String email, String password) {
        String passwordDB = userService.getPasswordByEmail(email);
        return passwordEncoder.matches(password, passwordDB);
    }
}
