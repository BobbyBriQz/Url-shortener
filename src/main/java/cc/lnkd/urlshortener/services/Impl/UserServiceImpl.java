package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.configs.DBConfig;
import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.jwt.JwtUtil;
import cc.lnkd.urlshortener.models.AuthUser;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;
import cc.lnkd.urlshortener.repositories.UserRepository;
import cc.lnkd.urlshortener.services.MailService;
import cc.lnkd.urlshortener.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Random;

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
    public RegisteredUser register(RegistrationRequest request) throws SQLException, BadRequestException, MessagingException {
        if(emailAlreadyExists(request.getEmail())){
            throw new BadRequestException("Email '" +request.getEmail()+ "' has already been registered");
        }

        String verificationCode = generateVerificationCode();
        RegisteredUser registeredUser = new UserRepository(dbConfig).register(request, verificationCode);

        mailService.sendWelcomeEmail(registeredUser.getEmail(), verificationCode);

        return registeredUser;
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


    public boolean emailAlreadyExists(String email) throws SQLException {
        int emailCount= new UserRepository(dbConfig).doesEmailAlreadyExist(email);

        return emailCount > 0;
    }

    public String generateVerificationCode() {
        StringBuilder buffer = new StringBuilder(6);

        String numbers = "123456789";
        Random random = new Random();
        for( int i = 0; i< 6; i++){
            char x = numbers.charAt(random.nextInt(numbers.length()));
            buffer.append(x);
        }

        return buffer.toString();
    }

}
