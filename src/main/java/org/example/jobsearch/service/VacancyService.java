package org.example.jobsearch.service;

import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VacancyService {
    List<VacancyDto> getVacanciesByApplicantId(int id) throws VacancyNotFoundException, ResumeNotFoundException;

    List<VacancyDto> getVacancies();

    List<VacancyDto> getVacanciesByCategoryId(int id) throws VacancyNotFoundException;

    List<UserDto> getApplicantsByVacancyId(int id) throws UserNotFoundException, ResumeNotFoundException;

    void createVacancy(VacancyDto vacancyDto) throws VacancyException;

    VacancyDto getVacancyById(int id) throws VacancyNotFoundException;

    void editVacancy(int id, UpdateVacancyDto updateVacancyDto) throws VacancyException;

    void deleteVacancyById(int id);

    List<RespondedResumeDto> getRespondedResumesByVacancyId(int id);

    List<VacancyDto> getVacanciesByQuery(String query);

    void respondToVacancy(RespondedApplicantDto respondedApplicantDto) throws ResumeException, VacancyException;
}
