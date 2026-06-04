package com.program.passholder.Endpoints.SecurityPassword.ResetSecurityPassword;

import com.program.passholder.Database.Querry.Password.PasswordEntity;

import java.util.List;

public class ResetSecurityPasswordDTO {
    public String newSecurityPassword;
    public String code;
    public List<PasswordEntity> storage;
}
