package org.example.jobsearch.service;

import jakarta.servlet.http.HttpServletRequest;
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

    void acceptResponse(Long resume, Long vacancy, Authentication authentication);

    void denyResponse(Long resume, Long vacancy, Authentication authentication);

    String getEmployerNameById(Long id);

    Integer getCountOfVacancies(Long id);

    String getApplicantNameById(Long id);

    Integer getCountOfResumes(Long id);

    Long getRecipientId(Long respondedApplicantId, Long authorId);

    List<ResponseEmployerDto> getEmployerResponsesByVacancyId(Authentication authentication, Long vacancyId);
}
