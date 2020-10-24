package cc.lnkd.urlshortener.validations;

import cc.lnkd.urlshortener.exceptions.BadRequestException;
import cc.lnkd.urlshortener.models.request.AuthRequest;
import cc.lnkd.urlshortener.models.request.LinkRequest;
import cc.lnkd.urlshortener.models.request.RegistrationRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RequestValidator {

    public static void validateCreateLinkRequest(LinkRequest request) throws BadRequestException {

        if(request.getUrl() == null || request.getUrl().isEmpty()){
            throw new BadRequestException("Url to be shortened is required");
        }
        if(!request.getUrl().substring(0, 8).equalsIgnoreCase("https://")
                && !request.getUrl().substring(0, 7).equalsIgnoreCase("http://")
        ){
            throw new BadRequestException("Please input 'https://' or 'http://' in your url");
        }
    }

    public static void validateAuthRequest(AuthRequest request) throws BadRequestException {

        if (request.getEmail() == null || request.getEmail().isEmpty()){
            throw new BadRequestException("Email is required");
        }else {
            boolean isValidEmail = ValidationUtils.validate(request.getEmail());
            if(!isValidEmail){
                throw new BadRequestException("Email provided is invalid");
            }
        }
        if(request.getPassword() == null || request.getPassword().isEmpty()){
            throw new BadRequestException("Password is required");
        }
    }

    public static void validateStringParam(String verificationCode) throws BadRequestException {

        if(verificationCode == null || verificationCode.isEmpty()){

            throw new BadRequestException("Verification code is required");
        }
    }
}
