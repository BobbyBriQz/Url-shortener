package cc.lnkd.urlshortener.controllers;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.response.APIResponse;
import cc.lnkd.urlshortener.models.request.LinkRequest;
import cc.lnkd.urlshortener.models.response.LinkResponse;
import cc.lnkd.urlshortener.services.LinkService;
import cc.lnkd.urlshortener.validations.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/links")
public class LinkController {

    @Autowired
    LinkService linkService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse> create(@RequestBody LinkRequest request) throws BadRequestException, SQLException {

        RequestValidator.validateCreateLinkRequest(request);

        LinkResponse response = linkService.writeURLToDBForFree(request);

        APIResponse apiResponse = new APIResponse(true, "Short url was generated successfully", response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
