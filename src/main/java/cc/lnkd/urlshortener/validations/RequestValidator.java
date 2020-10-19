package cc.lnkd.urlshortener.validations;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.request.LinkRequest;


public class RequestValidator {

    public static void validateLinkRequest(LinkRequest request) throws BadRequestException {

        if(request.getUrl() == null || request.getUrl().isEmpty()){
            throw new BadRequestException("Url to be shortened is required");
        }
        if(!request.getUrl().substring(0, 8).equalsIgnoreCase("https://")
                && !request.getUrl().substring(0, 7).equalsIgnoreCase("http://")
        ){
            throw new BadRequestException("Please input 'https://' or 'http://' in your url");
        }
    }
}
