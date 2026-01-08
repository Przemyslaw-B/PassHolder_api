package com.program.passholder.Database.Querry.User.User;

import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Encryption.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateNewUser {
    private final UserRepository userRepository;
    private final Encoder hash;

    @Autowired
    public CreateNewUser(UserRepository userRepository, Encoder hash) {
        this.userRepository = userRepository;
        this.hash = hash;
    }

    public void createNewUser(String email, String name, String password) {
        System.out.println("Dodaję nowe konto do DB: " + email + ", " +  name + ", " + password);
        String hashPass = hash.passwordEncoder().encode(password);  //Hashowanie hasła
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(hashPass);
        userRepository.save(newUser);
    }
}
