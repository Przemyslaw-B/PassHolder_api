package com.program.passholder.Database.Querry.AuditLogs.Logs;

import com.program.passholder.Database.Querry.AuditLogs.Events.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LogRepository extends JpaRepository<LogEntity, Long> {
    Optional<LogEntity> findById(long id);
    List<LogEntity> findAllByIdEvent(int id_event);
    List<LogEntity> findAllByIdUser(long id_user);
    List<LogEntity> findAllByIp(String ip);
}
