package org.example.jobsearch.service;

import org.example.jobsearch.dto.ContactDto;
import org.example.jobsearch.dto.MessageDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    List<ContactDto> messagesGet(Authentication auth);

    List<MessageDto> messageGetByRespondedApplicantId(Long id);
}
