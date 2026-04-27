package com.program.passholder.Endpoints.GetFiltersData;

import com.program.passholder.Admins.GetAllAdminsDetails;
import com.program.passholder.Database.Querry.AuditLogs.Events.EventEntity;
import com.program.passholder.Database.Querry.AuditLogs.Events.EventService;
import com.program.passholder.Database.Querry.Roles.RoleService;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/getFiltersData")
    public ResponseEntity<Map<String, Object>> GetFiltersData(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("Dane do filtrów...");
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);
            List<String> allAdminsEmails = adminsDetails.getAllAdminsDetails();
            List<EventEntity> eventsList = eventService.getAllEvents();
            //System.out.println("Lista do events: ...");
            //System.out.println("eventsList size: " + eventsList.size());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "events", eventsList, "administrators", allAdminsEmails));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }

}
