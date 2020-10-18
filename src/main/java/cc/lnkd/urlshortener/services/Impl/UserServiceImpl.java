package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.db.DBConfig;
import cc.lnkd.urlshortener.models.AuthUser;
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
}
