package com.program.passholder.Storage;

import com.program.passholder.Database.Querry.Password.PasswordRepository;
import com.program.passholder.Database.Querry.Password.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveStorage {
    @Autowired
    PasswordRepository passwordRepository;

    @Transactional
    public void removeStorage(long userId){
        passwordRepository.deleteByUserId(userId);
    }
}
