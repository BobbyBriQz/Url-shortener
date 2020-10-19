package cc.lnkd.urlshortener.controllers;

import cc.lnkd.urlshortener.jwt.JwtUtil;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;
import cc.lnkd.urlshortener.models.response.APIResponse;
import cc.lnkd.urlshortener.models.request.LoginRequest;
import cc.lnkd.urlshortener.models.response.LoginResponse;
import cc.lnkd.urlshortener.models.response.RegistrationResponse;
import cc.lnkd.urlshortener.services.UserService;
import cc.lnkd.urlshortener.validations.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired UserService userService;

    @Autowired PasswordEncoder passwordEncoder;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtUtil jwtTokenUtil;

    @Autowired private UserDetailsService userDetailsService;

    @PostMapping("/login")
    ResponseEntity<APIResponse> login(@RequestBody LoginRequest request) throws Exception {

        RequestValidator.validateAuthRequest(request);

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

        return ResponseEntity.ok(new APIResponse(true, "Login Successful", new LoginResponse(user, jwt)));

    }

    @PostMapping("/register")
    ResponseEntity<APIResponse> register(@RequestBody RegistrationRequest request) throws Exception {

        RequestValidator.validateAuthRequest(request);

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        RegisteredUser user = userService.register(request);
        if (user == null){
            throw new Exception("User could not be created");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new APIResponse(true, "User Registered Successfully", new RegistrationResponse(user, jwt)));
    }




}
