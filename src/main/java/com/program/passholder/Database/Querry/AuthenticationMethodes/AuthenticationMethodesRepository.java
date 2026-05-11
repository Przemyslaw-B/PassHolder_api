package com.program.passholder.Database.Querry.AuthenticationMethodes;

import com.program.passholder.Database.Querry.User.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AuthenticationMethodesRepository extends JpaRepository<AuthenticationMethodesEntity, Integer>,
        JpaSpecificationExecutor<AuthenticationMethodesEntity> {
    Optional<AuthenticationMethodesEntity> findAuthenticationMethodeByName(String name);
    Optional<AuthenticationMethodesEntity> findAuthenticationMethodeById(Integer id);
}
