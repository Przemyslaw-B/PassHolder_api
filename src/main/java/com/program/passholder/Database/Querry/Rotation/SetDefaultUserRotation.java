package com.program.passholder.Database.Querry.Rotation;

import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetDefaultUserRotation {
    private final UserRepository userRepository;
    private final UserService userService;


    @Autowired
    public SetDefaultUserRotation(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void setUserDefaultRotationByEmail(String email, int rotation){
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setRotation(rotation);
            userRepository.save(user);
        });
    }
}
