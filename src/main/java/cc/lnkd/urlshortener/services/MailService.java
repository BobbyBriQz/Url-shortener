package cc.lnkd.urlshortener.services;

import javax.mail.MessagingException;

public interface MailService {

    void sendWelcomeEmail(String to, String verificationCode) throws MessagingException;
}
