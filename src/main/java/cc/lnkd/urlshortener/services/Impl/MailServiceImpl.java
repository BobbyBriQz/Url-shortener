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

    @Override
    @Async("threadPoolTaskExecutor")
    public void sendWelcomeEmail(String to, String verificationCode) throws MessagingException {

        javaMailSender.setHost(emailConfig.getHost());
        javaMailSender.setPort(emailConfig.getPort());
        javaMailSender.setUsername(emailConfig.getUsername());
        javaMailSender.setPassword(emailConfig.getPassword());




        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("support@lnkd.cc");
        javaMailSender.send(message);*/

        // Prepare the evaluation context
        final Context ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("name", to);
        ctx.setVariable("verificationCode", verificationCode);


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


        // Send mail
        this.javaMailSender.send(mimeMessage);


    }

}
