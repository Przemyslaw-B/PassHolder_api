package com.program.passholder.Database.Querry.User.Authentication;

import com.program.passholder.Database.Querry.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetAuthKey {
    private final UserRepository userRepository;

    @Autowired
    public SetAuthKey(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setAuthKey(String email, String authKey) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setAuthKey(authKey);
            userRepository.save(user);
        });
    }
}
