package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.models.AuthUser;

import java.sql.SQLException;

public interface UserService {

    AuthUser getAuthUserByEmail(String email) throws SQLException;
}
