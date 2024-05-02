package org.example.jobsearch.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.models.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VacancyService {

    List<VacancyDto> getVacancies();

    List<VacancyDto> getActiveVacancies();

    List<VacancyDto> getInActiveVacancies();

    List<VacancyDto> getVacanciesByCategoryId(Long id) throws VacancyNotFoundException;

    void createVacancy(Authentication auth, VacancyDto vacancyDto);

    VacancyDto getVacancyById(Long id) throws VacancyNotFoundException;

    void editVacancy(Long id, UpdateVacancyDto updateVacancyDto);

    void deleteVacancyById(Long id);

    void deleteVacancyById(Long id, Authentication authentication);

    List<RespondedResumeDto> getRespondedResumesByVacancyId(Long id);

    List<VacancyDto> getVacanciesByQuery(String query);

    void respondToVacancy(RespondedApplicantDto respondedApplicantDto);

    List<ProfileAndVacancyDto> getVacanciesByEmployerName(String employer);

    List<Vacancy> getVacanciesByEmployerId(Long id);

    void update(Long id);
    Integer getCount();

    List<PageVacancyDto> getActivePageVacancies();
    Page<PageVacancyDto> getActivePageVacancies(Integer pageNumber);

    PageVacancyDto getPageVacancyById(Long id);

    Long addVacancyFromForm(CreatePageVacancyDto vacancyPageDto, HttpServletRequest request, Authentication auth);

    PageVacancyDto vacancyEditGet(Long id, Authentication authentication);

    Long editVacancyFromForm(UpdatePageVacancyDto vacancyDto, HttpServletRequest request, Authentication auth);

    List<PageVacancyDto> getPageVacancyByCategoryId(Long categoryId);
    Page<PageVacancyDto> getPageVacancyByCategoryId(Long categoryId, int page);

    String getNameById(Long vacancyId);

    List<AjaxResumeDto> getResumesForVacancy(ResumesForVacancyDto resumesForVacancyDto);

    List<ProfilePageVacancyDto> getPageVacanciesByAuthorId(Long id);
}
