package com.program.passholder.Database.Querry.User.Authentication;

import com.program.passholder.Database.Querry.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetAuthKey {
    private final UserRepository userRepository;

    @Autowired
    public SetAuthKey(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void setAuthKey(String email, String authKey) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setAuthKey(authKey);
            userRepository.save(user);
        });
    }

    @Transactional
    public void setTotpSecret(String email, String secret) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setTotpSecret(secret);
            userRepository.save(user);
        });
    }
}
