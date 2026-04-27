package com.program.passholder.Admins;

import com.program.passholder.Database.Querry.Roles.RoleEntity;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GetAllAdminsDetails {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    public List<String> getAllAdminsDetails(){
    List<String> detailsList = new ArrayList<>();
    detailsList = getAdminsDetails();
    return detailsList;
    }


    private List<UserRoleEntity> getAllAdmins(List<RoleEntity> roles) {
        List<UserRoleEntity> admins = new ArrayList<>();
        if(roles!=null && roles.size()>0){
            for (RoleEntity role : roles) {
                if(role!= null && (role.getName().equals("master") || role.getName().equals("system_admin") || role.getName().equals("head_admin"))){
                    int roleId = role.getId();
                    List<UserRoleEntity> adminsTemp = userRoleService.findAllByIdRole(roleId);
                    admins.addAll(adminsTemp);
                }
            }
        }
        return admins;
    }

    private List<String> getAdminsDetails() {
        List<RoleEntity> roles = roleService.getAll();
        List<UserRoleEntity> admins = getAllAdmins(roles);
        List<String> details = new ArrayList<>();
        if(admins!=null && admins.size()>0){
            for (UserRoleEntity admin : admins) {
                long userId = admin.getIdUser();
                Optional<UserEntity> detailTemp = userService.getEntityByid(userId);
                if(detailTemp.isPresent()){
                    details.add(detailTemp.get().getEmail());
                }
            }
        }
        return details;
    }
}

