package com.program.passholder.Database.Querry.Roles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findById(int id);
    Optional<RoleEntity> findByName(String name);
}
