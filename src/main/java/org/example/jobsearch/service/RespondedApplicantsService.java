package org.example.jobsearch.service;

import org.example.jobsearch.dto.ResponseApplicantDto;
import org.example.jobsearch.dto.ResponseEmployerDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RespondedApplicantsService {
    List<ResponseApplicantDto> getApplicantResponsesByUser(Authentication authentication);

    Integer getApprovedResponsesNumber(Authentication authentication);

    List<ResponseEmployerDto> getEmployerResponsesByUser(Authentication authentication);
}
