package com.program.passholder.Database.Querry.AuditLogs.Logs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
