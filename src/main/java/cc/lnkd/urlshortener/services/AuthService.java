package cc.lnkd.urlshortener.services;

import cc.lnkd.urlshortener.exceptions.BadRequestException;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface AuthService {

    public boolean resendVerificationCode(String email) throws SQLException, BadRequestException, MessagingException;
}
