package cc.lnkd.urlshortener.controllers;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.LinkResponse;
import cc.lnkd.urlshortener.services.LinkService;
import cc.lnkd.urlshortener.validations.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.sql.SQLException;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    LinkService linkService;

    @GetMapping("/*")
    ResponseEntity<String> gotoUrl(HttpServletRequest request) throws SQLException, BadRequestException {

        String slug = request.getRequestURI().substring(1);

        LinkResponse data = linkService.retrieveURLFromDB(slug.trim());
        if(data == null){
            //Todo: display beautiful 404 page
            throw new BadRequestException("Slug does not exist in db");
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(data.getUrl()))
                .build();
    }
}
