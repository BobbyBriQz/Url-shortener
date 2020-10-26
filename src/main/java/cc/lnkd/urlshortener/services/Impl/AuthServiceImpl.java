package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.configs.DBConfig;
import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.jwt.JwtUtil;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.LoginRequest;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;
import cc.lnkd.urlshortener.models.response.LoginResponse;
import cc.lnkd.urlshortener.models.response.RegistrationResponse;
import cc.lnkd.urlshortener.repositories.UserRepository;
import cc.lnkd.urlshortener.services.AuthService;
import cc.lnkd.urlshortener.services.MailService;
import cc.lnkd.urlshortener.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired DBConfig dbConfig;

    @Autowired MailService mailService;

    @Autowired UserService userService;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired private JwtUtil jwtTokenUtil;

    @Autowired private UserDetailsService userDetailsService;

    @Autowired private AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect email or password", e);
        }

        RegisteredUser user = userService.getUserWithEmail(request.getEmail());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new LoginResponse(user, jwt);

    }

    @Override
    public RegistrationResponse register(RegistrationRequest request) throws SQLException, BadRequestException, MessagingException {
        if(emailAlreadyExists(request.getEmail())){
            throw new BadRequestException("Email '" +request.getEmail()+ "' has already been registered");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        String verificationCode = generateRandomCode();
        RegisteredUser registeredUser = new UserRepository(dbConfig).register(request, verificationCode);

        if (registeredUser == null){
            throw new BadRequestException("User could not be created");
        }

        mailService.sendWelcomeEmail(registeredUser.getEmail(), verificationCode);


        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return new RegistrationResponse(registeredUser, jwt);
    }

    @Override
    public void resendVerificationCode(String email) throws SQLException, BadRequestException, MessagingException {
        if(!emailAlreadyExists(email)){
            throw new BadRequestException("Email not registered");
        }

        String verificationCode = new UserRepository(dbConfig).getVerificationCodeByEmail(email);

        mailService.resendVerificationCode(email, verificationCode);
    }

    @Override
    public void passwordReset(String email) throws Exception {
        if(!emailAlreadyExists(email)){
            throw new BadRequestException("Email not registered");
        }

        String resetPassword = generateRandomCode();

        int updateSuccessful = new UserRepository(dbConfig).updateUserPassword(email, passwordEncoder.encode(resetPassword));

        if (updateSuccessful == 0){
            throw new BadRequestException("Password could not be reset");
        }

        mailService.sendPasswordChangeEmail(email, resetPassword);
    }

    private boolean emailAlreadyExists(String email) throws SQLException {
        return new UserRepository(dbConfig).doesEmailAlreadyExist(email);
    }

    public String generateRandomCode() {
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
