package com.program.passholder.Database.Querry.Password;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordEntity,Long> {
    List<PasswordEntity> findByUserId(Long user_id);
    Optional<PasswordEntity> findById(Long id);
    Optional<PasswordEntity> findByIdAndUserId(Long id, Long user_id);
    void deleteByIdAndUserId(Long id, Long user_id);
}
