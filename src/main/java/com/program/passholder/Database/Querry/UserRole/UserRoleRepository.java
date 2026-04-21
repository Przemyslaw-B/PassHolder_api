package com.program.passholder.Database.Querry.UserRole;

import com.program.passholder.Database.Querry.Roles.RoleEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findById(long id);
    List<UserRoleEntity> findByIdUser(long idUser);
    List<UserRoleEntity> findBySettedBy(long settedBy);
    List<UserRoleEntity> findByIdRole(int idRole);
}
