package com.example.sportal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender emailSender;

    public void sendMessage(String to, String subject, String text) {
        new Thread(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("dontforgetyourpass@abv.bg");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        }).start();
    }
}
