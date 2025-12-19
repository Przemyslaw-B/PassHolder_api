package com.program.passholder.Database.Querry.User.PrivateKey;

import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetPrivateKeyByEmail {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public SetPrivateKeyByEmail(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    public void setPrivateKeyByEmail(String email, String privateKey){
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setPrivateKey(privateKey);
            userRepository.save(user);
        });
    }
}
