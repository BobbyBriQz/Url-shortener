package cc.lnkd.urlshortener.controllers;

import cc.lnkd.urlshortener.models.request.RegistrationRequest;
import cc.lnkd.urlshortener.models.response.APIResponse;
import cc.lnkd.urlshortener.models.request.LoginRequest;
import cc.lnkd.urlshortener.models.response.LoginResponse;
import cc.lnkd.urlshortener.models.response.RegistrationResponse;
import cc.lnkd.urlshortener.services.AuthService;
import cc.lnkd.urlshortener.validations.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired AuthService authService;


    @PostMapping("/login")
    ResponseEntity<APIResponse> login(@RequestBody LoginRequest request) throws Exception {

        RequestValidator.validateAuthRequest(request);

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(new APIResponse(true, "Login Successful", response));
    }

    @PostMapping("/register")
    ResponseEntity<APIResponse> register(@RequestBody RegistrationRequest request) throws Exception {

        RequestValidator.validateAuthRequest(request);

        RegistrationResponse response = authService.register(request);

        return ResponseEntity.ok(new APIResponse(true, "User Registered Successfully", response));
    }


    @GetMapping("/resend-verification-code")
    ResponseEntity<APIResponse> resendVerificationCode(@RequestParam("email") String email) throws Exception {

        RequestValidator.validateStringParam(email);

        authService.resendVerificationCode(email);

        return ResponseEntity.ok(new APIResponse(true, "User Verification code resent Successfully", null));
    }

    @GetMapping("/password-reset")
    ResponseEntity<APIResponse> passwordReset(@RequestParam("email") String email) throws Exception {

        RequestValidator.validateStringParam(email);

        authService.passwordReset(email);

        return ResponseEntity.ok(new APIResponse(true, "Password reset Successfully", null));
    }

}
