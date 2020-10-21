package cc.lnkd.urlshortener.controllers;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.jwt.JwtUtil;
import cc.lnkd.urlshortener.models.response.APIResponse;
import cc.lnkd.urlshortener.models.request.LinkRequest;
import cc.lnkd.urlshortener.models.response.LinkResponse;
import cc.lnkd.urlshortener.services.LinkService;
import cc.lnkd.urlshortener.validations.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/links")
public class LinkController {

    @Autowired
    LinkService linkService;




    @PostMapping("/create")
    public ResponseEntity<APIResponse> create(@RequestBody LinkRequest request) throws BadRequestException, SQLException {

        RequestValidator.validateCreateLinkRequest(request);

        LinkResponse response = linkService.writeURLToDBForIncognito(request);

        APIResponse apiResponse = new APIResponse(true, "Short url was generated successfully", response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_FREE','ROLE_PAID', 'ROLE_ADMIN')")
    @PostMapping("/free/create")
    public ResponseEntity<APIResponse> createForFreeUsers(@RequestBody LinkRequest requestBody, HttpServletRequest request) throws BadRequestException, SQLException {

        RequestValidator.validateCreateLinkRequest(requestBody);

        LinkResponse response = linkService.writeURLToDBForFree(requestBody, request);

        APIResponse apiResponse = new APIResponse(true, "Short url was generated successfully", response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_PAID', 'ROLE_ADMIN')")
    @PostMapping("/paid/create")
    public ResponseEntity<APIResponse> createForPaid(@RequestBody LinkRequest requestBody , HttpServletRequest request) throws BadRequestException, SQLException {

        RequestValidator.validateCreateLinkRequest(requestBody);

        LinkResponse response = linkService.writeURLToDBForPaid(requestBody, request);

        APIResponse apiResponse = new APIResponse(true, "Short url was generated successfully", response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
