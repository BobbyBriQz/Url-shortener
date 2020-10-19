package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.db.DBConfig;
import cc.lnkd.urlshortener.models.AuthUser;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;
import cc.lnkd.urlshortener.repositories.UserRepository;
import cc.lnkd.urlshortener.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    DBConfig dbConfig;

    @Override
    public AuthUser getAuthUserByEmail(String email) throws SQLException {
        return new UserRepository(dbConfig).getAuthUserByEmail(email);
    }

    @Override
    public RegisteredUser register(RegistrationRequest request) throws SQLException {
        return new UserRepository(dbConfig).register(request);
    }

    @Override
    public RegisteredUser getUserWithEmail(String email) throws SQLException {
        return new UserRepository(dbConfig).getUserWithEmail(email);
    }

    //Todo: Write method to check db for email existence

}
