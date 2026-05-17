package com.program.passholder.Endpoints.Filters.GetRoleUsermailFilterData;

import com.program.passholder.Database.Querry.User.User.GetFromMail;
import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Session.JwtUtil;
import com.program.passholder.Users.GetUsersListFilteredByMail;
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
public class GetUsermailFilterData {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    GetFromMail getFromMail;
    @Autowired
    GetUsersListFilteredByMail getUsersListFilteredByMail;

    @PostMapping("/recieveFilteredUsermailList")
    public ResponseEntity<Map<String, Object>> getUsermailFilterData(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GetUserMailFilterDTO request){
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            String userMail = jwtUtil.extractUsername(token);
            long userId = getFromMail.getUserIdFromMail(userMail);
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Invalid"));
    }
}
