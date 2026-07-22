package com.program.passholder.Endpoints.Filters.GetLogFiltersData;

import com.program.passholder.Admins.GetAllAdminsDetails;
import com.program.passholder.Database.Querry.AuditLogs.Events.EventEntity;
import com.program.passholder.Database.Querry.AuditLogs.Events.EventService;
import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GetFiltersDataEndpoint {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    GetAllAdminsDetails adminsDetails;
    @Autowired
    EventService eventService;
    @Autowired
    SetNewLog setNewLog;

    @GetMapping("/getAuditFiltersData")
    public ResponseEntity<Map<String, Object>> GetFiltersData(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest httpRequest) {

        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = httpRequest.getRemoteAddr();
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);
            Optional<Integer> userRole = userRoleService.getRoleIdByUserId(userId);
            //brak uprawnień
            if(userRole.isEmpty() || userRole.get() < 2){
                //setNewLog.setLog(16, ip, userId);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
            }

            List<String> allAdminsEmails = adminsDetails.getAllAdminsDetails();
            List<EventEntity> eventsList = eventService.getAllEvents();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "events", eventsList, "administrators", allAdminsEmails));
        }
        //setNewLog.setLog(12,ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
