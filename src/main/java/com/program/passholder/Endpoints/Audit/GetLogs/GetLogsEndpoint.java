package com.program.passholder.Endpoints.Audit.GetLogs;

import com.program.passholder.Database.Querry.AuditLogs.Events.EventService;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogEntity;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogService;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Logs.GetLogs;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GetLogsEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    RoleService roleService;
    @Autowired
    LogService logService;
    @Autowired
    GetLogs getLogs;
    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;

    @PostMapping("/recieveLogs")
    public ResponseEntity<Map<String, Object>> getLogs(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GetLogsDTO request){
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);
            Map<String, Object> data= new HashMap<>();
            data.put("typeName", request.typeName);
            data.put("adminMail", request.adminMail);
            data.put("fromDate", request.fromDate);
            data.put("toDate", request.toDate);

            List<LogEntity> logList = logService.getAllLogs();
            if(logList == null || logList.isEmpty()){
                logList = new  ArrayList();
            }
            logList = getLogs.getFilteredLogList(data);
            Map<String, Object> singleRecordList = new HashMap<>();
            List<Map> finalList = new ArrayList<>();
            for(LogEntity log : logList){
                singleRecordList.clear();
                singleRecordList.put("recordId", log.getIdRecord());
                singleRecordList.put("userName", userService.getMailById(log.getUserId()));
                singleRecordList.put("settedBy", userService.getMailById(log.getSettedBy()));
                singleRecordList.put("eventName", eventService.getNameById(log.getIdEvent()));
                singleRecordList.put("ip", log.getIp());
                singleRecordList.put("timestamp", log.getTimestamp());
                singleRecordList.put("details", log.getDetails());
                finalList.add(singleRecordList);
            }
            /*
            System.out.println("Lista logów<size>: " + logList.size());
            for (LogEntity log : logList) {
                System.out.println(log.getId());
            }
            */
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "logs", finalList));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
