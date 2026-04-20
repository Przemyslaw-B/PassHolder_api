package com.program.passholder.Roles;

import com.program.passholder.Database.Querry.Roles.RoleEntity;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.UserRole.UserRoleEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoleValidation {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    public boolean checkRole(long idUser, String roleName){
        return roleValidation(idUser, roleName);
    }
    public boolean checkRole(long idUser, int idRole){
        return roleValidation(idUser, idRole);
    }

    public boolean checkUserRole(long idUser){
        return roleValidation(idUser, "user");
    }

    public boolean checkAdminRole(long idUser){
        return roleValidation(idUser, "system_admin");
    }

    public boolean checkHeadAdminRole(long idUser){
        return roleValidation(idUser, "system_head_admin");
    }


    private boolean roleValidation(long idUser, String roleName){
        int systemRoleId = getSystemRoleId(roleName);
        return hasRole(idUser, systemRoleId);
    }

    private boolean roleValidation(long idUser, int idRole){
        Optional<RoleEntity> roleEntity = roleService.findById(idRole);
        if(roleEntity.isPresent()){
            return hasRole(idUser, idRole);
        }
        return false;
    }

    private boolean hasRole(long idUser, int systemRoleId){
        Boolean hasRole = false;
        List<UserRoleEntity> userRoleEntityList = userRoleService.findAllByIdUser(idUser);
        if(!userRoleEntityList.isEmpty()){
            for(UserRoleEntity userRoleEntity:userRoleEntityList){
                if(userRoleEntity.getIdRole()==systemRoleId){
                    hasRole=true;
                }
            }
        }
        return hasRole;
    }

    private int getSystemRoleId(String roleName){
        Optional<RoleEntity> roleEntity= roleService.findByName(roleName);
        if(roleEntity.isPresent()){
            return roleEntity.get().getId();
        }
        return 0;
    }
}
