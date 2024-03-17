package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dao.CategoryDao;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dao.VacancyDao;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.dto.VacancyDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.exceptions.VacancyException;
import org.example.jobsearch.exceptions.VacancyNotFoundException;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.example.jobsearch.service.VacancyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;
    private final CategoryDao categoryDao;
    private final UserDao userDao;

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
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("Вакансий в данной категории не найдено");
        }
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<UserDto> getApplicantsByVacancyId(int id) throws UserNotFoundException, ResumeNotFoundException {
        List<User> users = vacancyDao.getApplicantsByVacancyId(id);
        if (users.isEmpty()) {
            throw new UserNotFoundException("На эту вакансию никто не откликался!");
        }
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(e -> userDtos.add(UserDto.builder()
                .name(e.getName())
                .surname(e.getSurname())
                .accountType(e.getAccountType())
                .build()));
        return userDtos;
    }

    @Override
    public void createVacancy(VacancyDto vacancyDto) throws VacancyException {
        if (!userDao.idIsExists(vacancyDto.getAuthorId())) {
            throw new VacancyException("Пользователя не существует!");
        }
        if (!userDao.userIsEmployer(vacancyDto.getAuthorId())) {
            throw new VacancyException("Пользователь не работодатель!");
        }
        if (Boolean.FALSE.equals(categoryDao.isExists(vacancyDto.getCategoryId()))) {
            throw new VacancyException("Выбранной категории не существует");
        }
        if (vacancyDto.getSalary() <= 0) {
            throw new VacancyException("Зарплата не может быть меньше или равна нулю!");
        }
        if (vacancyDto.getExpFrom() > vacancyDto.getExpTo()) {
            throw new VacancyException("Стартовый опыт работы не может быть больше окончательного!");
        }
        Vacancy vacancy = new Vacancy();
        vacancy.setName(vacancyDto.getName());
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setCategoryId(vacancyDto.getCategoryId());
        vacancy.setSalary(vacancyDto.getSalary());
        vacancy.setExpFrom(vacancyDto.getExpFrom());
        vacancy.setExpTo(vacancyDto.getExpTo());
        vacancy.setIsActive(vacancyDto.getIsActive());
        vacancy.setAuthorId(vacancyDto.getAuthorId());
        vacancy.setCreatedTime(LocalDateTime.now());
        vacancy.setUpdateTime(LocalDateTime.now());
        vacancyDao.createVacancy(vacancy);
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
