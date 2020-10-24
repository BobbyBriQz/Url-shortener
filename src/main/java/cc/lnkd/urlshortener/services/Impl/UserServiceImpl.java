package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.configs.DBConfig;
import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.jwt.JwtUtil;
import cc.lnkd.urlshortener.models.AuthUser;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.repositories.UserRepository;
import cc.lnkd.urlshortener.services.MailService;
import cc.lnkd.urlshortener.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    DBConfig dbConfig;

    @Autowired
    MailService mailService;

    @Autowired
    JwtUtil jwtUtil;


    @Override
    public AuthUser getAuthUserByEmail(String email) throws SQLException {
        return new UserRepository(dbConfig).getAuthUserByEmail(email);
    }

    @Override
    public RegisteredUser getUserWithEmail(String email) throws SQLException {
        return new UserRepository(dbConfig).getUserWithEmail(email);
    }

    @Override
    public RegisteredUser verifyUser(HttpServletRequest request, String verificationCode) throws SQLException, BadRequestException {

        final String authorizationHeader = request.getHeader("Authorization");

        int userId = 0;
        String jwt = authorizationHeader.substring(7);
        userId = jwtUtil.extractUserId(jwt);

        if(!new UserRepository(dbConfig).isVerificationCodeCorrect(userId, verificationCode)){

            throw new BadRequestException("Invalid Verification Code");
        }

        return new UserRepository(dbConfig).verifyUser(userId);
    }


}
