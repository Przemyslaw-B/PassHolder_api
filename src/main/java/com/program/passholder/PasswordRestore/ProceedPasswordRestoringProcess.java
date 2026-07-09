package com.program.passholder.PasswordRestore;

import com.program.passholder.Database.Querry.User.UserService;
import com.program.passholder.Mailings.Requests.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProceedPasswordRestoringProcess {
    @Autowired
    GeneratePasswordResetToken generatePasswordCode;
    @Autowired
    PasswordResetToken passwordResetCode;
    @Autowired
    UserService userService;

    public void proceedPasswordRestoringProcess(String email) {
        String code = generatePasswordCode.generatePasswordResetToken();    //generowanie kodu 9 znaków
        userService.setPasswordResetToken(email, code);                     //zapis kodu do bazy
        passwordResetCode.sendPasswordResetToken(email, code);              //wysyłka kodu mailem
    }
}
