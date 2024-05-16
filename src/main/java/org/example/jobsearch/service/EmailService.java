package org.example.jobsearch.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public interface EmailService {
    void sendEmail(String toEmail, String link, String nameOfUser) throws MessagingException, UnsupportedEncodingException;
}
