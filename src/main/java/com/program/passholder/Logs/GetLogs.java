package com.program.passholder.Logs;

import com.program.passholder.Database.Querry.AuditLogs.Events.EventService;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogEntity;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogRepository;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogService;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GetLogs {
    @Autowired
    LogService logService;
    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;
    @Autowired
    private LogRepository logRepository;

    public List<LogEntity> getFilteredLogList(Map<String, Object> data) {
        List<LogEntity> logList = new ArrayList<>();
        Integer type = null;
        Long adminId = null;
        Long userId = null;
        String ip = null;
        Date fromDate = null;
        Date toDate = null;

        if(data.get("typeName")!=null){
            Optional<Integer> typeOpt = eventService.getIdByName(data.get("typeName").toString());
            if(typeOpt.isPresent()){
                type = typeOpt.get();
            }
        }
        if(data.get("adminMail")!=null){
            adminId = userService.getUserIdByMail(data.get("adminMail").toString());
        }
        if(data.get("userMail")!=null){
            userId =  userService.getUserIdByMail(data.get("userMail").toString());
        }
        if(data.get("ip")!=null){
            ip =  data.get("ip").toString();
        }
        if(data.get("fromDate") != null){
            fromDate = (Date) data.get("fromDate");
            System.out.println("fromDate: " + fromDate);
        }
        if(data.get("toDate")!=null){
            toDate = (Date) data.get("toDate");
            System.out.println("toDate: " + toDate);
        }

        Specification<LogEntity> spec = Specification.unrestricted();
        if(type!=null){
           spec = spec.and(logService.hasType(type));
        }
        if(adminId != null){
            spec = spec.and(logService.hasSettedBy(adminId));
        }
        if(userId != null){
            spec = spec.and(logService.hasUser(userId));
        }
        if(ip != null && !ip.isBlank()){
            spec = spec.and(logService.hasIp(ip));
        }
        if(fromDate != null){
            spec = spec.and(logService.hasDateAfter(fromDate));
        }
        if(toDate != null){
            spec = spec.and(logService.hasDateBefore(toDate));
        }
        logList = logRepository.findAll(spec);
        return logList;
    }


}
