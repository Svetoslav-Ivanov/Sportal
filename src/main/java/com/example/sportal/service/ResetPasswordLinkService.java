package com.example.sportal.service;

import com.example.sportal.repository.ResetPasswordLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ResetPasswordLinkService {
    @Autowired
    ResetPasswordLinkRepository resetPasswordLinkRepository;


    public boolean existsById(String id) {
        return resetPasswordLinkRepository.existsById(id);
    }
}
