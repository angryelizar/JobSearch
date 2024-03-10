package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dao.VacancyDao;
import org.example.jobsearch.dto.VacancyDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.exceptions.VacancyNotFoundException;
import org.example.jobsearch.models.Vacancy;
import org.example.jobsearch.service.VacancyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;

    @Override
    public List<VacancyDto> getVacanciesByApplicantId(int id) throws VacancyNotFoundException, ResumeNotFoundException {
        List<Vacancy> vacancies = vacancyDao.getVacanciesByApplicantId(id);
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("Пользователь либо не откликался на вакансии - либо его нет :(");
        }
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<VacancyDto> getVacancies() {
        List<Vacancy> vacancies = vacancyDao.getVacancies();
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryId(int id) throws VacancyNotFoundException {
        List<Vacancy> vacancies = vacancyDao.getVacanciesByCategoryId(id);
        if (vacancies.isEmpty()){
            throw new VacancyNotFoundException("Вакансий в данной категории не найдено");
        }
        return getVacancyDtos(vacancies);
    }

    private List<VacancyDto> getVacancyDtos(List<Vacancy> vacancies) {
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        vacancies.forEach(e -> vacancyDtos.add(VacancyDto.builder()
                .name(e.getName())
                .description(e.getDescription())
                .categoryId(e.getId())
                .salary(e.getSalary())
                .expFrom(e.getExpFrom())
                .expTo(e.getExpTo())
                .isActive(e.getIsActive())
                .authorId(e.getAuthorId())
                .createdTime(e.getCreatedTime())
                .updateTime(e.getUpdateTime())
                .build()));
        return vacancyDtos;
    }
}
