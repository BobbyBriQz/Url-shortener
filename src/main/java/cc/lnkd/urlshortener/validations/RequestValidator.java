package cc.lnkd.urlshortener.validations;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.LinkRequest;


public class RequestValidator {

    public static void validateLinkRequest(LinkRequest request) throws BadRequestException {

        if(request.getUrl() == null || request.getUrl().isEmpty()){
            throw new BadRequestException(false, "Url is required", request);
        }
        if(!request.getUrl().substring(0, 8).equalsIgnoreCase("https://")
                && !request.getUrl().substring(0, 7).equalsIgnoreCase("http://")
        ){
            throw new BadRequestException(false, "Please input 'https://' or 'http://' in your url", request);
        }
    }
}