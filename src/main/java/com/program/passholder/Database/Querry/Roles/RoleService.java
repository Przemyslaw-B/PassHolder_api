package com.program.passholder.Database.Querry.Roles;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<RoleEntity> findById(int id) {
        return roleRepository.findById(id);
    }

    public Optional<RoleEntity> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
