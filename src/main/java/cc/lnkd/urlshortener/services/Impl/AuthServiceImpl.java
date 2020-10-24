package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.configs.DBConfig;
import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.repositories.UserRepository;
import cc.lnkd.urlshortener.services.AuthService;
import cc.lnkd.urlshortener.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.SQLException;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    DBConfig dbConfig;

    @Autowired
    MailService mailService;


    @Override
    public boolean resendVerificationCode(String email) throws SQLException, BadRequestException, MessagingException {
        if(!emailAlreadyExists(email)){
            throw new BadRequestException("Email not registered");
        }

        String verificationCode = new UserRepository(dbConfig).getVerificationCodeByEmail(email);

        mailService.resendVerificationCode(email, verificationCode);

        return true;
    }

    private boolean emailAlreadyExists(String email) throws SQLException {
        return new UserRepository(dbConfig).doesEmailAlreadyExist(email);
    }
}
