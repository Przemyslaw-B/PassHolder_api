package com.program.passholder.Logs;

import com.program.passholder.Database.Querry.AuditLogs.Logs.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveNewLog {
    @Autowired
    LogService logService;

    public void saveNewLog(int idEvent, long idUser, String ip, String details){
        logService.setNewLog(idEvent, idUser, ip, details);
    }

    public void saveNewLog(int idEvent, String ip, String details){
        logService.setNewLog(idEvent, ip, details);
    }

}
