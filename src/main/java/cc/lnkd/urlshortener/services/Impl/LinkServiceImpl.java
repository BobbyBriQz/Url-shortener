package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.db.DBConfig;
import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.jwt.JwtUtil;
import cc.lnkd.urlshortener.models.request.LinkRequest;
import cc.lnkd.urlshortener.models.response.LinkResponse;
import cc.lnkd.urlshortener.repositories.LinkRepository;
import cc.lnkd.urlshortener.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Random;

@Service
public class LinkServiceImpl implements LinkService {

    //Todo: Write user_id on link creation

    @Autowired
    DBConfig dbConfig;

    @Autowired private JwtUtil jwtTokenUtil;

    @Override
    public LinkResponse retrieveURLFromDB(String slug) throws SQLException {
        return new LinkRepository(dbConfig).retrieveURLFromDB(slug);
    }

    @Override
    public LinkResponse writeURLToDBForPaid(LinkRequest requestBody, HttpServletRequest request) throws SQLException, BadRequestException {
        int writeIndex = 0;

        int userId = getUserIdFromRequestJwt(request);

        //If slug is not provided
        if(requestBody.getSlug() == null || requestBody.getSlug().isEmpty()){
            do{
                //If slug length is provided
                if (requestBody.getSlugLength() != 0) {
                    requestBody.setSlug(generateSlug(requestBody.getSlugLength()));
                } else {
                    requestBody.setSlug(generateSlug(4));
                }

                //This catches the exception thrown when Duplicate entry in db Exception occurs
                try {
                    if(userId == 0) {
                        //UserId could not be gotten from JWT
                        writeIndex = new LinkRepository(dbConfig).writeURLToDB(requestBody.getUrl(), requestBody.getSlug());
                    }else{
                        writeIndex = new LinkRepository(dbConfig).writeURLToDB(requestBody.getUrl(), requestBody.getSlug(), userId);
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }while (writeIndex == 0);

            System.out.println("Slug generated for " +requestBody.getUrl()+ " is " + requestBody.getSlug());
        }else {
            //Slug was provided

            try {
                if(userId == 0) {
                    writeIndex = new LinkRepository(dbConfig).writeURLToDB(requestBody.getUrl(), requestBody.getSlug());
                }else {
                    writeIndex = new LinkRepository(dbConfig).writeURLToDB(requestBody.getUrl(), requestBody.getSlug(), userId);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            if(writeIndex == 0){
                throw new BadRequestException("Custom link has already been taken, please select another");
            }
        }
        return retrieveLinkResponseFromDB(writeIndex);
    }

    @Override
    public LinkResponse writeURLToDBForFree(LinkRequest requestBody, HttpServletRequest request) throws SQLException, BadRequestException {

        int writeIndex = 0;
        int userId = getUserIdFromRequestJwt(request);
        //If slug is not provided
        if(requestBody.getSlug() == null || requestBody.getSlug().isEmpty()){
            do{
                //If slug length is provided
                if (requestBody.getSlugLength() != 0) {
                    requestBody.setSlug(generateSlug(requestBody.getSlugLength()));
                } else {
                    requestBody.setSlug(generateSlug(6));
                }

                //This catches the exception thrown when Duplicate entry in db Exception occurs
                try {
                    if(userId == 0) {
                        writeIndex = new LinkRepository(dbConfig).writeURLToDB(requestBody.getUrl(), requestBody.getSlug());
                    }else {
                        writeIndex = new LinkRepository(dbConfig).writeURLToDB(requestBody.getUrl(), requestBody.getSlug(), userId);
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }while (writeIndex == 0);

            System.out.println("Slug generated for " +requestBody.getUrl()+ " is " + requestBody.getSlug());
        }else {
            //Slug was provided
            if (requestBody.getSlug().length() < 5){
                throw new BadRequestException("Free Users can't create slugs of less than 5 characters");
            }

            try {
                if(userId == 0) {
                    writeIndex = new LinkRepository(dbConfig).writeURLToDB(requestBody.getUrl(), requestBody.getSlug());
                }else {
                    writeIndex = new LinkRepository(dbConfig).writeURLToDB(requestBody.getUrl(), requestBody.getSlug(), userId);
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            if(writeIndex == 0){
                throw new BadRequestException("Custom link has already been taken, please select another");
            }
        }
        return retrieveLinkResponseFromDB(writeIndex);
    }

    @Override
    public LinkResponse writeURLToDBForIncognito(LinkRequest request) throws SQLException, BadRequestException {

        int writeIndex = 0;
        boolean urlExists = checkURLExistenceInDB(request.getUrl());
        if(urlExists){
            throw new BadRequestException("A short link already exists in our db for that url, Please signin/signup to generate a short link for this url");
        }

       do{
            //This catches the exception thrown when Duplicate slug entry in db Exception occurs
            try {
                writeIndex = new LinkRepository(dbConfig).writeURLToDB(request.getUrl(), generateSlug(8));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }while (writeIndex == 0);

        System.out.println("Slug generated for " +request.getUrl()+ " is " + request.getSlug());

        return retrieveLinkResponseFromDB(writeIndex);
    }


    LinkResponse retrieveLinkResponseFromDB(int id) throws SQLException{
        return new LinkRepository(dbConfig).retrieveURLFromDB(id);
    }

    public boolean checkURLExistenceInDB(String url) throws SQLException {
        int state = new LinkRepository(dbConfig).checkURLExistenceInDB(url);
        return state > 0;
    }

    public int getUserIdFromRequestJwt(HttpServletRequest request){
        int userId = 0;
        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            userId =  jwtTokenUtil.extractUserId(jwt);
            System.out.println("Extracted userId is: " + userId);
        }
        return userId;
    }


    public String generateSlug(int targetStringLength) {
        StringBuilder buffer = new StringBuilder(targetStringLength);

        String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        for( int i = 0; i< targetStringLength; i++){
            char x = alphabets.charAt(random.nextInt(alphabets.length()));
            buffer.append(x);
        }

        return buffer.toString();
    }


}
