package org.example.jobsearch.service;

import org.example.jobsearch.dto.ResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RespondedApplicantsService {
    List<ResponseDto> getResponsesByUser(Authentication authentication);
}
