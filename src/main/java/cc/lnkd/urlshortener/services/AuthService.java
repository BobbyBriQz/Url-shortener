package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.LoginRequest;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface AuthService {

    RegisteredUser login(LoginRequest loginRequest) throws Exception;
    RegisteredUser register(RegistrationRequest registrationRequest) throws SQLException, BadRequestException, MessagingException;
    void resendVerificationCode(String email) throws SQLException, BadRequestException, MessagingException;
}
