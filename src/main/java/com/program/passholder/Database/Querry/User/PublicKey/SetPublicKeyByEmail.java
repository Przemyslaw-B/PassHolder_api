package com.program.passholder.Database.Querry.User.PublicKey;

import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetPublicKeyByEmail {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public SetPublicKeyByEmail(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void setPublicKeyByEmail(String email, String publicKey){
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setPublicKey(publicKey);
            userRepository.save(user);
        });
    }
}
