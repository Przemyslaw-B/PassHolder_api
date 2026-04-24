package com.program.passholder.Database.Querry.UserRole;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public Optional<UserRoleEntity> findById(long id) {
        return userRoleRepository.findById(id);
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

    public List<UserRoleEntity> findAll() {
        return userRoleRepository.findAll();
    }
}
