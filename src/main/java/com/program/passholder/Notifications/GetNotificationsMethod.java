package com.program.passholder.Notifications;

import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetNotificationsMethod {
    @Autowired
    UserService userService;

    public Optional<Integer> getMethod(String userMail){
        Optional<UserEntity> userEntity = userService.getEntityByMail(userMail);
        if(userEntity.isPresent()) {
            int methodId = userEntity.get().getNotificationMethod();
            return Optional.of(methodId);
        }
        return Optional.empty();
    }

    public Optional<Integer> getMethod(long userId){
        Optional<UserEntity> userEntity = userService.getEntityByid(userId);
        if(userEntity.isPresent()) {
            int methodId = userEntity.get().getNotificationMethod();
            return Optional.of(methodId);
        }
        return Optional.empty();
    }
}
