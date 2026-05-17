package com.program.passholder.Users;

import com.program.passholder.Database.Querry.User.UserEntity;
import com.program.passholder.Database.Querry.User.UserRepository;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class GetUsersListFilteredByMail {
    @Autowired
    UserRepository userRepository;

    public List<UserEntity> getFilteredUserList(String data) {
        List<UserEntity> list = new ArrayList<>();
        Pageable limit = PageRequest.of(0, 20); //Limit znalezionych pozycji
        if(data != null && data.trim().length() >= 3) {
            data = data.toLowerCase().trim();
            list = userRepository.findAll(UserService.hasUser(data),limit).getContent();
            return list;
        }
        return Collections.emptyList();
    }
}
