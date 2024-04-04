package org.example.jobsearch.service;

import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.models.Vacancy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VacancyService {

    List<VacancyDto> getVacanciesByApplicantId(Long id) throws VacancyNotFoundException, ResumeNotFoundException;

    List<VacancyDto> getVacancies();

    List<VacancyDto> getActiveVacancies();

    List<VacancyDto> getInActiveVacancies();

    List<VacancyDto> getVacanciesByCategoryId(Long id) throws VacancyNotFoundException;

    List<UserDto> getApplicantsByVacancyId(Long id) throws UserNotFoundException, ResumeNotFoundException;

    void createVacancy(Authentication auth, VacancyDto vacancyDto);

    VacancyDto getVacancyById(Long id) throws VacancyNotFoundException;

    void editVacancy(Long id, UpdateVacancyDto updateVacancyDto);

    void deleteVacancyById(Long id);

    List<RespondedResumeDto> getRespondedResumesByVacancyId(Long id);

    List<VacancyDto> getVacanciesByQuery(String query);

    void respondToVacancy(RespondedApplicantDto respondedApplicantDto);

    List<ProfileAndVacancyDto> getVacanciesByEmployerName(String employer);

    List<Vacancy> getVacanciesByEmployerId(Long id);
    void update(Long id);
}
