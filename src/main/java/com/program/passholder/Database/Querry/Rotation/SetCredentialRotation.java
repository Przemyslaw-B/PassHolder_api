package com.program.passholder.Database.Querry.Rotation;

import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordRepository;
import com.program.passholder.Database.Querry.Password.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetCredentialRotation {

    private final PasswordService passwordService;
    private final PasswordRepository passwordRepository;

    @Autowired
    public SetCredentialRotation(PasswordService passwordService, PasswordRepository passwordRepository) {
        this.passwordService=passwordService;
        this.passwordRepository=passwordRepository;
    }

    @Transactional
    public void SetCredentialRotationById(long id, int rotation){
        PasswordEntity entity = passwordRepository.findById(id).orElseThrow(()->new RuntimeException("Password with requested id not found"));
        entity.setRotation(rotation);
    }

}
