package com.program.passholder.Endpoints.GetUserStorageEndpoint;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.program.passholder.Database.Querry.Password.GetStorage.GetStorageByUserId;
import com.program.passholder.Database.Querry.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoadUserStorage {
    String email;
    Map<Integer, Map> recordsList= new HashMap();
    //Map<String, String> record = new HashMap();
    @Autowired
    UserService userService;
    @Autowired
    GetStorageByUserId getStorageByUserId;
    @Autowired
    ParseStorageToJson parseStorageToJson;

    public LoadUserStorage(UserService userService) {
        this.userService = userService;
    }

    public List loadUserStorage(String email){
        this.email=email;
        Long userId = getUserId(email);
        clearMaps();    //Wyczyść mapę
        if(userId != null && userId>0){
            //System.out.println("Szukam storage dla id:" + userId);
            List storage = getStorageByUserId.getStorageByUserId(userId);
            if(storage!=null){
                //String json = parseStorageToJson.parseToJson(storage);
                return parseStorageToJson.parseToJson(storage);
            }
        }
        //jeśli nie ma użytkownika, zwróć pustą mapę
        return null;
    }

    private Long getUserId(String email){
        Long id=userService.getUserIdByMail(email);
            if(id==null){
                System.out.println("Dla użytkownika " + email + " nie odnaleziono wyników. return="+id);
                return null;
            }
        return id;
    }

    private void clearMaps(){
        recordsList.clear();
    }
}
