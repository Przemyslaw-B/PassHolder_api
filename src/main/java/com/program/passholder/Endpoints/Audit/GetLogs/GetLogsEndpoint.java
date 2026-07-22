package com.program.passholder.Endpoints.Audit.GetLogs;

import com.program.passholder.Database.Querry.AuditLogs.Events.EventService;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogEntity;
import com.program.passholder.Database.Querry.AuditLogs.Logs.LogService;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Logs.GetLogs;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class GetLogsEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    LogService logService;
    @Autowired
    GetLogs getLogs;
    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SetNewLog setNewLog;

    @PostMapping("/recieveLogs")
    public ResponseEntity<Map<String, Object>> getLogs(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GetLogsDTO request,
            HttpServletRequest httpRequest){

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);

            Optional<Integer> userRole =userRoleService.getRoleIdByUserId(userId);
            if(userRole.isEmpty() || userRole.get() < 2){
                setNewLog.setLog(16, ip, userId);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
            }

            Map<String, Object> data= new HashMap<>();
            data.put("typeName", request.typeName);
            data.put("adminMail", request.adminMail);
            data.put("ip", request.ip);
            data.put("fromDate", request.fromDate);
            data.put("toDate", request.toDate);

            List<LogEntity> logList = logService.getAllLogs();
            if(logList == null || logList.isEmpty()){
                logList = new  ArrayList();
            }
            logList = getLogs.getFilteredLogList(data);
            List<Map> finalList = new ArrayList<>();
            int pageNumber = request.pageNumber;
            int rowsAmount = request.rowsAmount;
            if(logList.isEmpty() ||  logList.size() < rowsAmount){
                pageNumber = 1;
            }
            int listSize = logList.size();
            boolean lastPage=false;
            int maxPageAmount = (int) Math.ceil((double) listSize / rowsAmount);
            if(pageNumber >= maxPageAmount){
                lastPage = true;
                pageNumber=maxPageAmount;
            }
            if(logList.isEmpty()){
                pageNumber = 1;
            }
            long counter = 0;
            long counterStart=pageNumber*rowsAmount;
            long counterEnd=counterStart+rowsAmount;
            for(LogEntity log : logList){
                if(counter>=counterStart && counter<counterEnd){
                    Map<String, Object> singleRecordList = new HashMap<>();
                    singleRecordList.put("recordId", log.getIdRecord());
                    singleRecordList.put("userName", userService.getMailById(log.getUserId()));
                    singleRecordList.put("settedBy", userService.getMailById(log.getSettedBy()));
                    singleRecordList.put("eventName", eventService.getNameById(log.getIdEvent()));
                    singleRecordList.put("ip", log.getIp());
                    singleRecordList.put("timestamp", log.getTimestamp());
                    singleRecordList.put("details", log.getDetails());
                    finalList.add(singleRecordList);
                }
                counter++;
            }
            //setNewLog.setLog(1,ip,userId,userId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "logs", finalList, "pageNumber", pageNumber, "lastPage", lastPage));
        }
        //setNewLog.setLog(12, ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
