package com.program.passholder.Endpoints.Filters.GetRoleUsermailFilterData;

import com.program.passholder.Database.Querry.AuditLogs.SetNewLog;
import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.UserRole.UserRoleService;
import com.program.passholder.Session.JwtUtil;
import com.program.passholder.Users.GetUsersListFilteredByMail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class GetUsermailFilterData {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    GetUsersListFilteredByMail getUsersListFilteredByMail;
    @Autowired
    SetNewLog setNewLog;
    @Autowired
    UserRoleService userRoleService;

    @PostMapping("/recieveFilteredUsermailList")
    public ResponseEntity<Map<String, Object>> getUsermailFilterData(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GetUserMailFilterDTO request,
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
                //setNewLog.setLog(16, ip, userId);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "brak uprawnień"));
            }

            List<String> data = new ArrayList<>();
            if(request.userMail != null && request.userMail.length()>=3){
                List<UserEntity> list = new ArrayList<>();
                list = getUsersListFilteredByMail.getFilteredUserList(request.userMail);
                if(list!=null && list.size()>0){
                    for(UserEntity entity : list){
                        data.add(entity.getEmail());
                    }
                }
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "ok", "data", data));
            }
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "fail", "error", "to short"));
        }
        //setNewLog.setLog(12,ip);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
