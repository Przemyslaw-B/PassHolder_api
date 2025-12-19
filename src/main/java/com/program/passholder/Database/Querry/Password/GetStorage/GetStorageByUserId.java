package com.program.passholder.Database.Querry.Password.GetStorage;

import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetStorageByUserId {
    @Autowired
    PasswordService passwordService;

    public GetStorageByUserId(PasswordService passwordService){
        this.passwordService = passwordService;
    }

    public List<PasswordEntity> getStorageByUserId(Long userId){
        return passwordService.getStorageByUserId(userId);
    }
}
