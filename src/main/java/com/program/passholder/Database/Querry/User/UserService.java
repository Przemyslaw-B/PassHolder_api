package com.program.passholder.Database.Querry.User;

import com.program.passholder.Database.Querry.AuditLogs.Logs.LogEntity;
import com.program.passholder.Database.Querry.User.User.SetSecurityPassword;
import com.program.passholder.Endpoints.SetSecurityPassword.SetSecurityPasswordEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    SetSecurityPassword setSecurityPassword;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public Optional<UserEntity> getEntityByid(long id) {
        return userRepository.findById(id);
    }

    public String getMailById(long id){
        return userRepository.findById(id)
                .map(UserEntity::getEmail)
                .orElse(null);
    }

    public String getSecurityPasswordById(long id){
        return userRepository.findById(id)
                .map(UserEntity::getSecurity_password)
                .orElse(null);
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

    public void setSecurityPassword(long userId, String securityPassword){
        setSecurityPassword.setUserSecurityPassword(userId, securityPassword);
    }

    public boolean isUserExist(String email){
        String get = userRepository.findByEmail(email)
                .map(UserEntity::getEmail)
                .orElse("");
        if(get.isEmpty()){
            return false;
        }
        return true;
    }

    /*
    public static Specification<UserEntity> hasUser(String userMail) {
        System.out.println("Szukan w UserService maila:" + userMail);
        return (root, query, cb) ->
                userMail == null ? null : cb.like(root.get("email"), "%" + userMail.toLowerCase().trim() + "%");
    }
    */


}
