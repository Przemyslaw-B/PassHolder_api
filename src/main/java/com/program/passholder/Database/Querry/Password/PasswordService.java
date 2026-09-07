package com.program.passholder.Database.Querry.Password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordService {
    @Autowired
    private PasswordRepository passwordRepository;

    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public List<PasswordEntity> getStorageByUserId(Long user_id){
        return passwordRepository.findByUserIdOrderByIdAsc(user_id);
    }

    public Optional<PasswordEntity> getPasswordByIdAndUserId(Long id, Long user_id){
        return passwordRepository.findByIdAndUserId(id, user_id);
    }

    public Optional<PasswordEntity> getEntityById(Long id){
        return passwordRepository.findById(id);
    }

    @Transactional
    public void setStoragePassword(long id, long userId, String newPassword){
        passwordRepository.findByIdAndUserId(id, userId).ifPresent(password -> {
            password.setPassword(newPassword);
        });
    }

    @Transactional
    public void setStorageUrl(long id, long userId, String newUrl){
        passwordRepository.findByIdAndUserId(id, userId).ifPresent(password -> {
            password.setUrl(newUrl);
        });
    }

    @Transactional
    public void setStorageLogin(long id, long userId, String newLogin){
        passwordRepository.findByIdAndUserId(id, userId).ifPresent(password -> {
            password.setLogin(newLogin);
        });
    }

}
