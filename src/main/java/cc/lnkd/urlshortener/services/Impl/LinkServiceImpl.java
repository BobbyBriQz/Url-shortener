package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.LinkRequest;
import cc.lnkd.urlshortener.models.LinkResponse;
import cc.lnkd.urlshortener.repositories.LinkRepository;
import cc.lnkd.urlshortener.services.LinkService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Random;

@Service
public class LinkServiceImpl implements LinkService {
    @Override
    public LinkResponse retrieveURLFromDB(String slug) throws SQLException {
        return new LinkRepository().retrieveURLFromDB(slug);
    }

    @Override
    public LinkResponse writeURLToDBForPaid(LinkRequest request) throws SQLException, BadRequestException {
        return null;
    }

    @Override
    public LinkResponse writeURLToDBForFree(LinkRequest request) throws SQLException, BadRequestException {
        LinkRepository repo = new LinkRepository();
        int writeIndex = 0;
        //If slug is not provided
        if(request.getSlug() == null || request.getSlug().isEmpty()){
            do{
                //If slug length is provided
                if (request.getSlugLength() != 0) {
                    request.setSlug(generateSlug(request.getSlugLength()));
                } else {
                    request.setSlug(generateSlug(5));
                }

                //This catches the exception thrown when Duplicate entry in db Exception occurs
                try {
                    writeIndex = repo.writeURLToDB(request.getUrl(), request.getSlug());
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }while (writeIndex == 0);

            System.out.println("Slug generated for " +request.getUrl()+ " is " + request.getSlug());
        }else {
            //Slug was provided

            try {
                writeIndex = repo.writeURLToDB(request.getUrl(), request.getSlug());
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
        LinkRepository repo = new LinkRepository();
        int writeIndex = 0;
        boolean urlExists = checkURLExistenceInDB(request.getUrl());
        if(urlExists){
            throw new BadRequestException("A short link already exists in our db for that url, Please signin/signup to generate a short link for this url");
        }

       do{
            //This catches the exception thrown when Duplicate entry in db Exception occurs
            try {
                writeIndex = repo.writeURLToDB(request.getUrl(), generateSlug(8));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }while (writeIndex == 0);

        System.out.println("Slug generated for " +request.getUrl()+ " is " + request.getSlug());

        return retrieveLinkResponseFromDB(writeIndex);
    }


    LinkResponse retrieveLinkResponseFromDB(int id) throws SQLException{
        return new LinkRepository().retrieveURLFromDB(id);
    }

    public boolean checkURLExistenceInDB(String url) throws SQLException {
        int state = new LinkRepository().checkURLExistenceInDB(url);
        return state != 0;
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
