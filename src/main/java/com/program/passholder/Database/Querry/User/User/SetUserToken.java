package com.program.passholder.Database.Querry.User.User;

import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetUserToken {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public SetUserToken(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void setTokenToUser(String email, String token) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setToken(token);
            userRepository.save(user);
        });
    }
}
