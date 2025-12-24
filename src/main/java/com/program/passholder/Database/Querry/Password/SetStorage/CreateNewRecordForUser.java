package com.program.passholder.Database.Querry.Password.SetStorage;

import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordRepository;
import com.program.passholder.Database.Querry.Password.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class CreateNewRecordForUser {
    private final PasswordRepository passwordRepository;
    private final PasswordService passwordService;

    @Autowired
    public CreateNewRecordForUser(PasswordService passwordService, PasswordRepository passwordRepository){
        this.passwordService = passwordService;
        this.passwordRepository = passwordRepository;
    }

    public long setNewPasswordRecordForUser(long userId, String url, String login, String password, Date expDate, Date modifyDate){
        PasswordEntity newRecord = new PasswordEntity();
        newRecord.setUserId(userId);
        newRecord.setUrl(url);
        newRecord.setLogin(login);
        newRecord.setPassword(password);
        newRecord.setExpDate(expDate);
        newRecord.setModifyDate(modifyDate);
        PasswordEntity savedPassword = passwordRepository.save(newRecord);
        return savedPassword.getId();   //Zwraca id_cloud zapisanego rekordu
    }

}


