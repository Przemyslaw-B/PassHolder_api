package com.program.passholder.Database.Querry.UserRole;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public Optional<UserRoleEntity> findByUserId(long idUser) {
        return userRoleRepository.findRecordByIdUser(idUser);
    }

    public List<UserRoleEntity> findAllByIdUser(long idUser) {
        return userRoleRepository.findByIdUser(idUser);
    }

    public List<UserRoleEntity> findAllByIdRole(int idRole) {
        return userRoleRepository.findByIdRole(idRole);
    }

    public List<UserRoleEntity> findAllByIdSettedBy(long settedBy) {
        return userRoleRepository.findBySettedBy(settedBy);
    }

    public Optional<Integer> getRoleIdByUserId(long idUser) {
        Optional<UserRoleEntity> entity = userRoleRepository.findRecordByIdUser(idUser);
        if(entity.isPresent()) {
            return Optional.of(entity.get().getIdRole());
        }
        return Optional.empty();
    }

    @Transactional
    public boolean changeUserRole(long userId, int newRoleId) {
        if (userId <= 0 || newRoleId <= 0) {
            return false;
        }
        return userRoleRepository.findRecordByIdUser(userId)
                .map(userRole -> {
                    userRole.setIdRole(newRoleId);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean changeUserRole(long userId, int newRoleId, long settedBy) {
        if (userId <= 0 || newRoleId <= 0) {
            return false;
        }
        return userRoleRepository.findRecordByIdUser(userId)
                .map(userRole -> {
                    userRole.setIdRole(newRoleId);
                    userRole.setSettedBy(settedBy);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean changeUserRoleToDefaultUser(long idUser) {
        if (idUser <= 0) {
            return false;
        }
        return userRoleRepository.findRecordByIdUser(idUser)
                .map(userRole -> {
                    userRole.setIdRole(1);
                    return true;
                })
                .orElse(false);
    }

    public List<UserRoleEntity> findAll() {
        return userRoleRepository.findAll();
    }

    public List<UserRoleEntity> findAllAdmins() {
        int userId = 1;
        List<UserRoleEntity> list = new ArrayList<UserRoleEntity>();
        List<UserRoleEntity> fullList = userRoleRepository.findAll();
        for(UserRoleEntity picked : fullList) {
            if(picked.getIdRole()!=userId) {
                list.add(picked);
            }
        }
        return list;
    }

}
