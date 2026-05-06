package com.program.passholder.Endpoints.Roles;

import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetRoleFromUserMail {
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;

    public Optional<Integer> getRoleId(String mail) {
        if(!mail.isBlank()) {
            long userId = userService.getUserIdByMail(mail);
            Optional <UserRoleEntity> userRole = userRoleService.findByUserId(userId);
            if(userRole.isPresent()) {
                int userRoleId = userRole.get().getIdRole();
                return Optional.of(userRoleId);
            }
        }
        return Optional.empty();
    }


}
