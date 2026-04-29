package com.program.passholder.Endpoints.GetLogs;

import com.program.passholder.Database.Querry.AuditLogs.Logs.LogEntity;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogService;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Logs.GetLogs;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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
            //data.put("userLogMail", request.userMail);
            data.put("fromDate", request.fromDate);
            data.put("toDate", request.toDate);

            List<LogEntity> logList = logService.getAllLogs();
            if(logList == null || logList.isEmpty()){
                logList = new  ArrayList();
            }
            logList = getLogs.getFilteredLogList(data);
            /*
            System.out.println("Lista logów<size>: " + logList.size());
            for (LogEntity log : logList) {
                System.out.println(log.getId());
            }
            */
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "logs", logList));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
