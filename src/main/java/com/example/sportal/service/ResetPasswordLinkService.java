package com.example.sportal.service;

import com.example.sportal.repository.ResetPasswordLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;


public class ResetPasswordLinkService {
    @Autowired
    ResetPasswordLinkRepository resetPasswordLinkRepository;


    public boolean existsById(String id) {
        return resetPasswordLinkRepository.existsById(id);
    }

    public void clearExpiredLinks() {
        resetPasswordLinkRepository.clearExpiredLinks();
    }
}

