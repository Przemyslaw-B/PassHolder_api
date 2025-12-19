package com.program.passholder.Database.Querry.Password;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordEntity,Long> {
    List<PasswordEntity> findByUserId(Long id);
}
