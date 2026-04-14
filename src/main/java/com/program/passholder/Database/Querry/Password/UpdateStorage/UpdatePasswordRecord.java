package com.program.passholder.Database.Querry.Password.UpdateStorage;

import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class UpdatePasswordRecord {
    @Autowired
    PasswordRepository passwordRepository;

    public boolean setFullDataForRecord(long id, long userId, String newUrl, String newLogin, String newPassword, int newRotation){
        PasswordEntity entity = getEntity(id, userId);
        if(entity == null){
            entity.setUrl(newUrl);
            entity.setLogin(newLogin);
            entity.setPassword(newPassword);
            entity.setRotation(newRotation);
            passwordRepository.save(entity);
            return true;
        }
        return false;
    }


    public boolean setUrl(Long id, Long userId, String url){
        PasswordEntity entity = getEntity(id, userId);
        if(entity != null){
            entity.setUrl(url);
            passwordRepository.save(entity);
            return true;
        }
        return false;
    }

    public boolean setLogin(long id, long userId, String login){
        PasswordEntity entity = getEntity(id, userId);
        if(entity != null){
            entity.setLogin(login);
            passwordRepository.save(entity);
            return true;
        }
        return false;
    }

    public boolean setPassword(long id, long userId, String password){
        PasswordEntity entity = getEntity(id, userId);
        if(entity != null){
            entity.setPassword(password);
            passwordRepository.save(entity);
            return true;
        }
        return false;
    }

    public boolean setRotation(long id, long userId, int rotation){
        PasswordEntity entity = getEntity(id, userId);
        if(entity != null){
            entity.setRotation(rotation);
            passwordRepository.save(entity);
            return true;
        }
        return false;
    }

    private PasswordEntity getEntity(long id, long userId){
        Optional<PasswordEntity> opt = passwordRepository.findById(id);
        if(opt.isPresent()){
            PasswordEntity entity = opt.get();
            if(Objects.equals(entity.getUserId(), userId)){
                return entity;
            }
        }
        return null;
    }



}
