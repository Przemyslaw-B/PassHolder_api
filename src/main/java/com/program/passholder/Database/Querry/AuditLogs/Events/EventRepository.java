package com.program.passholder.Database.Querry.AuditLogs.Events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface EventRepository extends JpaRepository<EventEntity, Integer> {
    Optional<EventEntity> findById(int id);
    Optional<EventEntity> findByName(String name);
}

