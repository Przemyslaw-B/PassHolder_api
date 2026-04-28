package com.program.passholder.Database.Querry.AuditLogs.Logs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Optional<LogEntity> getEntityById(long id) {
        return logRepository.findById(id);
    }

    public List <LogEntity> getAllLogs() {
        return logRepository.findAll();
    }

    public List<LogEntity> getListOfEntityByIdEvent(int idEvent) {
        return logRepository.findAllByIdEvent(idEvent);
    }

    public List<LogEntity> getListOfEntityByIdUser(long idUser) {
        return logRepository.findAllByIdUser(idUser);
    }

    public List<LogEntity> getListOfEntityByIp(String ip) {
        return logRepository.findAllByIp(ip);
    }

    public void setNewLog(int idEvent, long idUser, String ip, String details) {
        LogEntity entity = new LogEntity();
        entity.setIdEvent(idEvent);
        entity.setUserId(idUser);
        entity.setIp(ip);
        if(details!= null && !details.isBlank()) {
            entity.setDetails(details);
        }
        logRepository.save(entity);
    }

    public void setNewLog(int idEvent, String ip, String details) {
        LogEntity entity = new LogEntity();
        entity.setIdEvent(idEvent);
        entity.setIp(ip);
        if(details!= null && !details.isBlank()) {
            entity.setDetails(details);
        }
        logRepository.save(entity);
    }

    public static Specification<LogEntity> hasType(Integer type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("idEvent"), type);
    }

    public static Specification<LogEntity> hasIp(String ip) {
        return (root, query, cb) ->
                ip == null ? null : cb.like(root.get("ip"), "%" + ip + "%");
    }

    public static Specification<LogEntity> hasUser(Long userId){
        return (root, query, cb) ->
                userId == null ? null : cb.equal(root.get("idUser"), userId);
    }

    public static Specification<LogEntity> hasSettedBy(Long settedBy){
        return (root, query, cb) ->
                settedBy == null ? null : cb.equal(root.get("settedBy"), settedBy);
    }

    public static Specification<LogEntity> hasDateAfter(Date from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("timestamp"), from);
    }

    public static Specification<LogEntity> hasDateBefore(Date to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("timestamp"), to);
    }
}
