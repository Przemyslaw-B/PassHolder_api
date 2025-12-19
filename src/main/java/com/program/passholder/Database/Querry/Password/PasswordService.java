package com.program.passholder.Database.Querry.Password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {
    @Autowired
    private PasswordRepository passwordRepository;

    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public List<PasswordEntity> getStorageByUserId(Long userId){
        return passwordRepository.findByUserId(userId);
    }
}
