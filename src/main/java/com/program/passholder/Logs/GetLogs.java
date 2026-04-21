package com.program.passholder.Logs;

import com.program.passholder.Database.Querry.AuditLogs.Logs.LogEntity;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class GetLogs {
    @Autowired
    LogService logService;

    public Optional<LogEntity> getLog(long idLog){
        //TODO
        return null;
    }

    public List<LogEntity> getAllLogsOfUser(long idUser) {
        //TODO
        return null;
    }

    public List<LogEntity> getAllLogsSettedBy(long idUser){
        //TODO
        return null;
    }

    public List<LogEntity> getAllLogsOfIp(String ip) {
        //TODO
        return null;
    }

    public List<LogEntity> getAllLogsOfEvent(int idEvent) {
        //TODO
        return null;
    }

    public List<LogEntity> getAllLogsStartedFrom(Timestamp timestamp){
        //TODO
        return null;
    }

    public List<LogEntity> getAllLogsFromMonth(int month){
        //TODO
        return null;
    }

    public List<LogEntity> getAllLogsFromYear(int year){
        //TODO
        return null;
    }

    public List<LogEntity> getAllLogsFromTo(Timestamp timestampStart, Timestamp timestampEnd){
        //TODO
        return null;
    }
}
