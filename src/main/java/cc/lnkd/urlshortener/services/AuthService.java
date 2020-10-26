package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.LoginRequest;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;
import cc.lnkd.urlshortener.models.response.LoginResponse;
import cc.lnkd.urlshortener.models.response.RegistrationResponse;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest) throws Exception;
    RegistrationResponse register(RegistrationRequest registrationRequest) throws SQLException, BadRequestException, MessagingException;
    void resendVerificationCode(String email) throws SQLException, BadRequestException, MessagingException;
    void passwordReset(String email) throws Exception;
}
