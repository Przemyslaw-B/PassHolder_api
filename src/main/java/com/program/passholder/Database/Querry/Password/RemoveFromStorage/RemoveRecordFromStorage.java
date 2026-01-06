package com.program.passholder.Database.Querry.Password.RemoveFromStorage;

import com.program.passholder.Database.Querry.Password.PasswordRepository;
import com.program.passholder.Database.Querry.Password.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveRecordFromStorage {
    private final PasswordService passwordService;
    private final PasswordRepository passwordRepository;

    @Autowired
    public RemoveRecordFromStorage(PasswordService passwordService, PasswordRepository passwordRepository) {
        this.passwordService = passwordService;
        this.passwordRepository = passwordRepository;
    }

    @Transactional
    public void removeRecordFromStorage(long userId, long passwordId) {
        passwordRepository.deleteByIdAndUserId(passwordId, userId);
    }
}
