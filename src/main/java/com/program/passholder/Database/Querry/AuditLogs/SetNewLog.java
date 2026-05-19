package com.program.passholder.Database.Querry.AuditLogs;

import com.program.passholder.Database.Querry.AuditLogs.Logs.LogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;

@Component
public class SetNewLog {
    LogService logService;

    @Autowired
    public SetNewLog(LogService logService){
        this.logService = logService;
    }

    public void setLog(int event, String ip, long userId, long settedBy, long recordId){
        logService.setNewLog(event, ip, userId, settedBy, recordId);
    }

    public void setLog(int event, String ip, long userId, long settedBy){
        logService.setNewLog(event, ip, userId, settedBy);
    }

    public void setLog(int event, String ip, long userId){
        logService.setNewLog(event, ip, userId);
    }

    public void setLog(int event, String ip){
        logService.setNewLog(event, ip);
    }
}
