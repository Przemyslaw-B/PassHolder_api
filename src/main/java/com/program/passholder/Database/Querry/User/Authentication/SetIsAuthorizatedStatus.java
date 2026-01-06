package com.program.passholder.Database.Querry.User.Authentication;

import com.program.passholder.Database.Querry.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetIsAuthorizatedStatus {
    private final UserRepository userRepository;

    @Autowired
    public SetIsAuthorizatedStatus(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setAuthStatus(String email, int authStatus){
        userRepository.findByEmail(email).ifPresent(user -> {
           user.setIsAuthorized(authStatus);
        });
    }
}
