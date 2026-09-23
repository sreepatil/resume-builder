package com.shubham.resumebuilder.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private final JavaMailSender javaMailSender;

    public void sendHtmlEmail(String to, String subject, String htmlContent)
            throws MessagingException {

        log.info("Sending email to: {}", to);
        log.info("SMTP username: {}", username);
        log.info("SMTP password loaded: {}",
                password != null && !password.isBlank());
        log.info("From email: {}", fromEmail);

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);

        log.info("Verification email sent successfully to: {}", to);
    }
}