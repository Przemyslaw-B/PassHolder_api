package com.program.passholder.Roles;

import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
public class GetRoleDetailsOfUsers {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    public List<Object> getDetails(List<UserRoleEntity> list){
        List<Object> detailsList = new ArrayList<>();
        if(list!=null && !list.isEmpty()){
            for(UserRoleEntity entity: list){
                long id = entity.getId();
                long userId = entity.getIdUser();
                int roleId = entity.getIdRole();
                long settedBy = entity.getSettedBy();
                String userMail = userService.getMailById(userId);
                String adminMail = userService.getMailById(userId);
                String roleName="";
                if(roleService.getById(roleId).isPresent()){
                    roleName = roleService.getById(roleId).get().getName();
                }
                Map<String,Object> map = new HashMap<>();
                map.put("id",id);
                map.put("userMail",userMail);
                map.put("adminMail",adminMail);
                map.put("roleName",roleName);
                detailsList.add(map);
            }
        }
        return detailsList;
    }
}
