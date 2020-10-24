package cc.lnkd.urlshortener.controllers;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.RegisteredUser;
import cc.lnkd.urlshortener.models.request.LinkRequest;
import cc.lnkd.urlshortener.models.response.APIResponse;
import cc.lnkd.urlshortener.models.response.LinkResponse;
import cc.lnkd.urlshortener.services.UserService;
import cc.lnkd.urlshortener.validations.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<APIResponse> verifyUser(HttpServletRequest request, @RequestParam String verificationCode) throws BadRequestException, SQLException {

        RequestValidator.validateVerificationCode(verificationCode);

        RegisteredUser registeredUser = userService.verifyUser(request, verificationCode);

        APIResponse apiResponse = new APIResponse(true, "User was verified successfully", registeredUser);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
