package com.example.sportal.config;

import com.example.sportal.service.ResetPasswordLinkService;
import com.example.sportal.util.validators.ArticleValidator;
import org.modelmapper.ModelMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Properties;

@Configuration
public class ApplicationBeenConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.abv.bg");
        mailSender.setPort(465);

        mailSender.setUsername("dontforgetyourpass@abv.bg");
        mailSender.setPassword("Sport@l!");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.fallback", "false");


        return mailSender;
    }

    @Bean
    public ResetPasswordLinkService resetPasswordLinkService() {
        return new ResetPasswordLinkService();
    }

    @Bean
    public ArticleValidator articleValidator() {
        return new ArticleValidator();
    }
}
