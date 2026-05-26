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
    public void setUserSecurityPassword(long userId, String securityPassword) {
            userRepository.findById(userId).ifPresent(user -> {
            user.setSecurity_password(securityPassword);
            userRepository.save(user);
        });
    }

}
