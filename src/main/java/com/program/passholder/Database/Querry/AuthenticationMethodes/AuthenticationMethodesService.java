package com.program.passholder.Database.Querry.AuthenticationMethodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationMethodesService {
    @Autowired
    AuthenticationMethodesRepository authenticationMethodesRepository;

    public Optional<AuthenticationMethodesEntity> getMethodeById(int id){
        return authenticationMethodesRepository.findAuthenticationMethodeById(id);
    }

    public Optional<AuthenticationMethodesEntity> getMethodeByName(String name){
        return authenticationMethodesRepository.findAuthenticationMethodeByName(name);
    }

    public List<AuthenticationMethodesEntity> getAllAuthenticationMethodes(){
        return authenticationMethodesRepository.findAll();
    }
}
