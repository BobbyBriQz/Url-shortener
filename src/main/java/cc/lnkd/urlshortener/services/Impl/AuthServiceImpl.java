package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.configs.DBConfig;
import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.LoginRequest;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;
import cc.lnkd.urlshortener.repositories.UserRepository;
import cc.lnkd.urlshortener.services.AuthService;
import cc.lnkd.urlshortener.services.MailService;
import cc.lnkd.urlshortener.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    DBConfig dbConfig;

    @Autowired
    MailService mailService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired private AuthenticationManager authenticationManager;

    @Override
    public RegisteredUser login(LoginRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect email or password", e);
        }

        return userService.getUserWithEmail(request.getEmail());
    }

    @Override
    public RegisteredUser register(RegistrationRequest request) throws SQLException, BadRequestException, MessagingException {
        if(emailAlreadyExists(request.getEmail())){
            throw new BadRequestException("Email '" +request.getEmail()+ "' has already been registered");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        String verificationCode = generateVerificationCode();
        RegisteredUser registeredUser = new UserRepository(dbConfig).register(request, verificationCode);

        if (registeredUser == null){
            throw new BadRequestException("User could not be created");
        }

        mailService.sendWelcomeEmail(registeredUser.getEmail(), verificationCode);

        return registeredUser;
    }

    @Override
    public void resendVerificationCode(String email) throws SQLException, BadRequestException, MessagingException {
        if(!emailAlreadyExists(email)){
            throw new BadRequestException("Email not registered");
        }

        String verificationCode = new UserRepository(dbConfig).getVerificationCodeByEmail(email);

        mailService.resendVerificationCode(email, verificationCode);
    }

    private boolean emailAlreadyExists(String email) throws SQLException {
        return new UserRepository(dbConfig).doesEmailAlreadyExist(email);
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
