package cc.lnkd.urlshortener.services.Impl;

import cc.lnkd.urlshortener.configs.EmailConfig;
import cc.lnkd.urlshortener.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Autowired
    EmailConfig emailConfig;

    @Autowired
    TemplateEngine templateEngine;

    private void configureMailsSender() {
        javaMailSender.setHost(emailConfig.getHost());
        javaMailSender.setPort(emailConfig.getPort());
        javaMailSender.setUsername(emailConfig.getUsername());
        javaMailSender.setPassword(emailConfig.getPassword());
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public void sendWelcomeEmail(String to, String verificationCode) throws MessagingException {

        configureMailsSender();

        // Prepare the evaluation context
        final Context ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("name", to);
        ctx.setVariable("verificationCode", verificationCode);
        //ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

        message.setSubject("Welcome to Lnkd.cc");
        message.setFrom("support@lnkd.cc");
        message.setTo(to);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("html/registrationMail.html", ctx);
        message.setText(htmlContent, true); // true = isHtml

        /*
        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        message.addInline(imageResourceName, imageSource, imageContentType);
        */

        // Send mail
        this.javaMailSender.send(mimeMessage);
    }


    @Override
    @Async("threadPoolTaskExecutor")
    public void resendVerificationCode(String to, String verificationCode) throws MessagingException {

        configureMailsSender();

        // Pass the values you want to access in ThymeLeaf html
        final Context ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("name", to);
        ctx.setVariable("verificationCode", verificationCode);
        //ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

        message.setSubject("Lnkd.cc Verification Code");
        message.setFrom("support@lnkd.cc");
        message.setTo(to);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("html/verificationMail.html", ctx);
        message.setText(htmlContent, true); // true = isHtml

        /*
        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        message.addInline(imageResourceName, imageSource, imageContentType);
        */

        // Send mail
        this.javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendPasswordChangeEmail(String email, String resetPassword) throws MessagingException {
        configureMailsSender();

        // Pass the values you want to access in ThymeLeaf html
        final Context ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("name", email);
        ctx.setVariable("password", resetPassword);
        //ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart

        message.setSubject("Lnkd.cc Password Reset");
        message.setFrom("support@lnkd.cc");
        message.setTo(email);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("html/passwordResetMail.html", ctx);
        message.setText(htmlContent, true); // true = isHtml

        /*
        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        message.addInline(imageResourceName, imageSource, imageContentType);
        */

        // Send mail
        this.javaMailSender.send(mimeMessage);
    }

}
