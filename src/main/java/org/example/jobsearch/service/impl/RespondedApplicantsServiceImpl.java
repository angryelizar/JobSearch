package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.RespondedApplicantDao;
import org.example.jobsearch.dto.ResponseDto;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.User;
import org.example.jobsearch.service.RespondedApplicantsService;
import org.example.jobsearch.service.ResumeService;
import org.example.jobsearch.service.UserService;
import org.example.jobsearch.service.VacancyService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RespondedApplicantsServiceImpl implements RespondedApplicantsService {
    private final RespondedApplicantDao respondedApplicantDao;
    private final UserService userService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    @Override
    @SneakyThrows
    public List<ResponseDto> getResponsesByUser(Authentication authentication) {
        User user = userService.getFullUserByEmail(authentication.getName());
        List<RespondApplicant> respondApplicants = new ArrayList<>(respondedApplicantDao.getByApplicantEmail(user.getEmail()));
        return getResponseDtos(respondApplicants);
    }

    @Override
    @SneakyThrows
    public Integer getApprovedResponsesNumber(Authentication authentication) {
        User user = userService.getFullUserByEmail(authentication.getName());
        return respondedApplicantDao.getApprovedResponsesNumber(user.getId());
    }

    public List<ResponseDto> getResponseDtos(List<RespondApplicant> list) {
        List<ResponseDto> responses = new ArrayList<>();
        for (RespondApplicant respondApplicant : list) {
            responses.add(ResponseDto.builder()
                    .resumedId(respondApplicant.getResumeId())
                    .resumeName(resumeService.getResumeNameById(respondApplicant.getResumeId()))
                    .vacancyName(vacancyService.getNameById(respondApplicant.getVacancyId()))
                    .vacancyId(respondApplicant.getVacancyId())
                    .status(getStatusByBoolean(respondApplicant.getConfirmation()))
                    .build());
        }
        return responses;
    }

    private String getStatusByBoolean(Boolean confirmation) {
        if (confirmation == null) {
            return "В обработке";
        } else if (confirmation) {
            return "Одобрено";
        } else {
            return "Отклонено";
        }
    }
}
