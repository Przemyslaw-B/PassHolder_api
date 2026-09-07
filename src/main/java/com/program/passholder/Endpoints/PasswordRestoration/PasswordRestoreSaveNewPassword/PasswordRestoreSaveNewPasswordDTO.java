package com.program.passholder.Endpoints.PasswordRestoration.PasswordRestoreSaveNewPassword;

import com.program.passholder.Database.Querry.Password.PasswordEntity;

import java.util.List;

public class PasswordRestoreSaveNewPasswordDTO {
    public String newPassword;
    public String email;
    public String passwordChangeToken;
    public String authCode;
    public List<PasswordEntity> storage;
    public Boolean dataRemove;
}
