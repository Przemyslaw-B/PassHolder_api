package com.program.passholder.Database.Querry.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public String getPasswordByEmail(String email){
        return userRepository.findByEmail(email)
                .map(UserEntity::getPassword)
                .orElse(null);  // wyjątek jeśli taki użytkownik nie istnieje.
    }

    public String getPublicKeyByEmail(String email){
        return userRepository.findByEmail(email)
                .map(UserEntity::getPublicKey)
                .orElse(null);  // wyjątek jeśli taki użytkownik nie istnieje.
    }

    public String getPrivateKeyByEmail(String email){
        return userRepository.findByEmail(email)
                .map(UserEntity::getPrivateKey)
                .orElse(null);
    }

    public String getNameByMail(String email){
        return userRepository.findByEmail(email)
                .map(UserEntity::getName)
                .orElse(null);
    }

    public String getTokenByMail(String email){
        return userRepository.findByEmail(email)
                .map(UserEntity::getToken)
                .orElse(null);
    }

    public Long getUserIdByMail(String email){
        return userRepository.findByEmail(email)
                .map(UserEntity::getId)
                .orElse(null);
    }

    public String getAuthKeyByMail(String email){
        return userRepository.findByEmail(email)
                .map(UserEntity::getAuthKey)
                .orElse(null);
    }

    public int isAuthNeeded(String email){
        return userRepository.findByEmail(email)
                .map(UserEntity::getIsAuthorized)
                .orElse(0);
    }


}
