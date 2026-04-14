package com.program.passholder.Database.Querry.Password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return passwordRepository.findByUserId(user_id);
    }

    public Optional<PasswordEntity> getPasswordByIdAndUserId(Long id, Long user_id){
        return passwordRepository.findByIdAndUserId(id, user_id);
    }
}
