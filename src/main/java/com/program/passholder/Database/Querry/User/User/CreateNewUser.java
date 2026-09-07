package com.program.passholder.Database.Querry.User.User;

import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleRepository;
import com.program.passholder.Encryption.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Component
public class CreateNewUser {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final Encoder hash;

    @Autowired
    public CreateNewUser(UserRepository userRepository, UserRoleRepository userRoleRepository, UserService userService, Encoder hash) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
        this.hash = hash;
    }

    @Transactional
    public void createNewUser(String email, String name, String password, String salt) {
        System.out.println("Dodaję nowe konto do DB: " + email + ", " +  name + ", " + password);
        String hashPass = hash.passwordEncoder().encode(password);  //Hashowanie hasła
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(hashPass);
        newUser.setSalt(salt);
        newUser.setNotificationMethod(1);
        userRepository.save(newUser);

        Optional<UserEntity> userEntity= userService.getEntityByMail(email);
        if(userEntity.isPresent()){
            UserRoleEntity newUserRole = new UserRoleEntity();
            newUserRole.setIdUser(userEntity.get().getId());
            newUserRole.setIdRole(1);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            newUserRole.setTimestamp(timestamp);
            userRoleRepository.save(newUserRole);
        }

    }
}
