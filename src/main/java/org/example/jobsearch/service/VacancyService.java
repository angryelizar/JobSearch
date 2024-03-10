package org.example.jobsearch.service;

import org.example.jobsearch.dto.VacancyDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.exceptions.VacancyNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VacancyService {
    List<VacancyDto> getVacanciesByApplicantId(int id) throws VacancyNotFoundException, ResumeNotFoundException;

    List<VacancyDto> getVacancies();

    List<VacancyDto> getVacanciesByCategoryId(int id) throws VacancyNotFoundException;
}
