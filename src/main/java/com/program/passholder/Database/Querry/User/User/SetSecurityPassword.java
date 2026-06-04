package com.program.passholder.Database.Querry.User.User;

import com.program.passholder.Database.Querry.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetSecurityPassword {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public boolean setUserSecurityPassword(long userId, String securityPassword) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setSecurity_password(securityPassword);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

}
