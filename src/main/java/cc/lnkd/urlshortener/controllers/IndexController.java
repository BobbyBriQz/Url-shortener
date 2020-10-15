package cc.lnkd.urlshortener.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping("/*")
    ResponseEntity<String> gotoUrl(HttpServletRequest request){

        /*String slug = request.getRequestURI().substring(1);
        new RequestValidator().validateSlug(slug);

        String urlFromDB = redirectService.retrieveURLFromDB(slug.trim());
        CreateRequest data = new CreateRequest(slug.trim(), urlFromDB);
        if(urlFromDB == null){

            //Todo: Debug why the error message is not showing
            throw new BadRequestException(false, "Slug does not exist in db", data);
        }*/

        /*return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlFromDB))
                .build();*/

        return new ResponseEntity<>(request.getRequestURI().substring(1), HttpStatus.OK);
    }
}
