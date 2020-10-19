package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.models.AuthUser;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;

import java.sql.SQLException;

public interface UserService {

    AuthUser getAuthUserByEmail(String email) throws SQLException;
    RegisteredUser register(RegistrationRequest registrationRequest) throws SQLException;
    RegisteredUser getUserWithEmail(String email) throws SQLException;

    //Todo: Write method to check db for email existence
}
