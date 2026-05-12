package com.program.passholder.Database.Querry.User.User;

import com.program.passholder.Database.Querry.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetPhoneNumber {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void setUserPhoneNumber(long userId, String phone) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setPhone(phone);
            userRepository.save(user);
        });
    }

    @Transactional
    public void setUserPhoneNumber(String email, String phone) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setPhone(phone);
            userRepository.save(user);
        });
    }
}
