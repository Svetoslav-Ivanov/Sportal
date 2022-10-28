package com.example.sportal.service;

import com.example.sportal.model.entity.ResetPasswordLink;
import com.example.sportal.repository.ResetPasswordLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ResetPasswordLinkService {
    @Autowired
    ResetPasswordLinkRepository resetPasswordLinkRepository;


    public boolean existsById(String id) {
        return resetPasswordLinkRepository.existsById(id);
    }

    public void clearExpiredLinks() {
        resetPasswordLinkRepository
                .findAll()
                .removeIf(l -> l.getExpiresAt()
                        .before(Calendar.getInstance().getTime()));
    }
}

