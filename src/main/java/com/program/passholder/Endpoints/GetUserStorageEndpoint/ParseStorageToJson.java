package com.program.passholder.Endpoints.GetUserStorageEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.program.passholder.Database.Querry.Password.PasswordEntity;
import com.program.passholder.Database.Querry.Password.PasswordRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParseStorageToJson {

    public  List parseToJson(List<PasswordEntity> storage){
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> resList = new ArrayList<>();
        for(PasswordEntity picked : storage){
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", picked.getId());
            userData.put("url", picked.getUrl());
            userData.put("login", picked.getLogin());
            userData.put("password", picked.getPassword());
            userData.put("expDate", picked.getExpDate());
            userData.put("modDate", picked.getModifyDate());
            resList.add(userData);
        }
        try{
            //return mapper.writeValueAsString(resList);
            return resList;
        }catch(Exception e){
            throw new RuntimeException("Json serialization error.", e);
        }

    }
}
