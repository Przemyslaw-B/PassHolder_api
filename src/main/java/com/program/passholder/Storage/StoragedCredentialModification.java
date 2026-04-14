package com.program.passholder.Storage;

import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordRepository;
import com.program.passholder.Database.Querry.Password.UpdateStorage.UpdatePasswordRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class StoragedCredentialModification {
    @Autowired
    UpdatePasswordRecord updatePasswordRecord;
    @Autowired
    PasswordRepository passwordRepository;

    private long recordId=0;
    private long userId=0;
    private String newUrl;
    private String newLogin;
    private String newPassword;
    private int newRotation;

    public boolean updateCredential(long recordId, long userId, String newUrl, String newLogin, String newPassword, int newRotation){
        this.recordId = recordId;
        this.userId = userId;
        this.newUrl = newUrl;
        this.newLogin = newLogin;
        this.newPassword = newPassword;
        this.newRotation = newRotation;
        Boolean wasModified = false;
        if(checkUrl() && setUrl()){
            wasModified = true;
        }
        if(checkLogin() && setLogin()){
            wasModified = true;
        }
        if(checkPassword() && setPassword()){
            wasModified = true;
        }
        if(checkRotation() && setRotation()){
            wasModified = true;
        }
        return wasModified;
    }

    private boolean setUrl(){
        if(recordId >0 && userId >0 && !newUrl.isEmpty()){
            Boolean result = updatePasswordRecord.setUrl(recordId, userId, newUrl);
            if(result){return true;}
        }
        return false;
    }

    private boolean checkUrl(){
        if(recordId >0 && userId >0 && !newUrl.isEmpty()){
            Optional<PasswordEntity> entity = passwordRepository.findByIdAndUserId(recordId, userId);
            if(entity.isPresent() && !Objects.equals(entity.get().getUrl(), newUrl) ){
                return true;
            }
        }
        return false;
    }

    private boolean setLogin(){
        if(recordId >0 && userId >0 && !newLogin.isEmpty()){
            Boolean result = updatePasswordRecord.setLogin(recordId, userId, newLogin);
            if(result){return true;}
        }
        return false;
    }

    private boolean checkLogin(){
        if(recordId >0 && userId >0 && !newLogin.isEmpty()){
            Optional<PasswordEntity> entity = passwordRepository.findByIdAndUserId(recordId, userId);
            if(entity.isPresent() && !Objects.equals(entity.get().getLogin(), newLogin) ){
                return true;
            }
        }
        return false;
    }

    private boolean setPassword(){
        if(recordId >0 && userId >0 && !newPassword.isEmpty()){
            Boolean result = updatePasswordRecord.setPassword(recordId, userId, newPassword);
            if(result){return true;}
        }
        return false;
    }

    private boolean checkPassword(){
        if(recordId >0 && userId >0 && !newPassword.isEmpty()){
            Optional<PasswordEntity> entity = passwordRepository.findByIdAndUserId(recordId, userId);
            if(entity.isPresent() && !Objects.equals(entity.get().getPassword(), newPassword) ){
                return true;
            }
        }
        return false;
    }

    private boolean setRotation(){
        if(recordId > 0 && userId > 0 && newRotation >= 0){
            Boolean result = updatePasswordRecord.setRotation(recordId, userId, newRotation);
            if(result){return true;}
        }
        return false;
    }

    private boolean checkRotation(){
        if(recordId >0 && userId >0 && newRotation >= 0){
            Optional<PasswordEntity> entity = passwordRepository.findByIdAndUserId(recordId, userId);
            if(entity.isPresent() && !Objects.equals(entity.get().getRotation(), newRotation) ){
                return true;
            }
        }
        return false;
    }


}
