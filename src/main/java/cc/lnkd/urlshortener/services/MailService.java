package cc.lnkd.urlshortener.services;

import javax.mail.MessagingException;

public interface MailService {

    void sendWelcomeEmail(String to, String verificationCode) throws MessagingException;

    void resendVerificationCode(String email, String verificationCode) throws MessagingException;
}
