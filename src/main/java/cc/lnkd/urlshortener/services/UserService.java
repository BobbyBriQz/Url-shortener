package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.AuthUser;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public interface UserService {

    AuthUser getAuthUserByEmail(String email) throws SQLException;
    RegisteredUser getUserWithEmail(String email) throws SQLException;
    RegisteredUser verifyUser(HttpServletRequest request, String verificationCode) throws SQLException, BadRequestException;

}
