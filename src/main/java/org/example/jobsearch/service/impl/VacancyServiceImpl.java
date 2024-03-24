package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.*;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.models.*;
import org.example.jobsearch.service.VacancyService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyServiceImpl implements VacancyService {
    private final VacancyDao vacancyDao;
    private final CategoryDao categoryDao;
    private final UserDao userDao;
    private final RespondedApplicantDao respondedApplicantDao;
    private final ResumeDao resumeDao;
    private final EducationInfoDao educationInfoDao;
    private final WorkExperienceInfoDao workExperienceInfoDao;

    @Override
    public List<VacancyDto> getVacanciesByApplicantId(Long id) throws VacancyNotFoundException, ResumeNotFoundException {
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
    public List<VacancyDto> getActiveVacancies(){
        List<Vacancy> vacancies = vacancyDao.getActiveVacancies();
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<VacancyDto> getInActiveVacancies(){
        List<Vacancy> vacancies = vacancyDao.getInActiveVacancies();
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryId(Long id) throws VacancyNotFoundException {
        List<Vacancy> vacancies = vacancyDao.getVacanciesByCategoryId(id);
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("Вакансий в данной категории не найдено");
        }
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<UserDto> getApplicantsByVacancyId(Long id) throws UserNotFoundException, ResumeNotFoundException {
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
    @SneakyThrows
    public void createVacancy(Authentication auth, VacancyDto vacancyDto) {
        if (Boolean.FALSE.equals(categoryDao.isExists(vacancyDto.getCategoryId()))) {
            throw new VacancyException("Выбранной категории не существует");
        }
        if (vacancyDto.getSalary() <= 0) {
            throw new VacancyException("Зарплата не может быть меньше или равна нулю!");
        }
        if (vacancyDto.getExpFrom() > vacancyDto.getExpTo()) {
            throw new VacancyException("Стартовый опыт работы не может быть больше окончательного!");
        }
        Vacancy vacancy = Vacancy.builder()
                .name(vacancyDto.getName())
                .description(vacancyDto.getDescription())
                .categoryId(vacancyDto.getCategoryId())
                .salary(vacancyDto.getSalary())
                .expFrom(vacancyDto.getExpFrom())
                .expTo(vacancyDto.getExpTo())
                .isActive(vacancyDto.getIsActive())
                .authorId(userDao.getUserByEmail(auth.getName()).get().getId())
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        vacancyDao.createVacancy(vacancy);
    }

    @Override
    public VacancyDto getVacancyById(Long id) throws VacancyNotFoundException {
        if (vacancyDao.getVacancyById(id).isEmpty()) {
            throw new VacancyNotFoundException("Вакансия не найдена");
        }
        return getVacancyDto(vacancyDao.getVacancyById(id).get());
    }

    @Override
    @SneakyThrows
    public void editVacancy(Long id, UpdateVacancyDto updateVacancyDto) {
        if (!vacancyDao.isExists(id)) {
            throw new VacancyException("Такой вакансии нет - нечего редактировать!");
        }
        if (Boolean.FALSE.equals(categoryDao.isExists(updateVacancyDto.getCategoryId()))) {
            throw new VacancyException("Выбранной категории не существует");
        }
        if (updateVacancyDto.getSalary() <= 0) {
            throw new VacancyException("Зарплата не может быть меньше или равна нулю!");
        }
        if (updateVacancyDto.getExpFrom() > updateVacancyDto.getExpTo()) {
            throw new VacancyException("Стартовый опыт работы не может быть больше окончательного!");
        }
        Vacancy vacancy = Vacancy.builder()
                .name(updateVacancyDto.getName())
                .description(updateVacancyDto.getDescription())
                .categoryId(updateVacancyDto.getCategoryId())
                .salary(updateVacancyDto.getSalary())
                .expFrom(updateVacancyDto.getExpFrom())
                .expTo(updateVacancyDto.getExpTo())
                .isActive(updateVacancyDto.getIsActive())
                .updateTime(LocalDateTime.now())
                .build();
        vacancyDao.editVacancy(id, vacancy);
    }

    @Override
    public void deleteVacancyById(Long id) {
        vacancyDao.deleteVacancyById(id);
    }

    @Override
    public List<RespondedResumeDto> getRespondedResumesByVacancyId(Long id) {
        List<RespondApplicant> respondedApplicants = new ArrayList<>(respondedApplicantDao.getRespondedApplicantsByVacancyId(id));
        List<RespondedResumeDto> respondedResumeDtos = new ArrayList<>();
        for (RespondApplicant respondedApplicant : respondedApplicants) {
            Resume resume = resumeDao.getResumeById(respondedApplicant.getResumeId()).get();
            respondedResumeDtos.add(
                    RespondedResumeDto.builder()
                            .name(resume.getName())
                            .applicantName(userDao.getUserNameById(resume.getApplicantId()))
                            .applicantSurname(userDao.getSurnameNameById(resume.getApplicantId()))
                            .category(categoryDao.getCategoryNameById(resume.getCategoryId()))
                            .salary(resume.getSalary())
                            .isActive(resume.getIsActive())
                            .createdTime(resume.getCreatedTime())
                            .updateTime(resume.getUpdateTime())
                            .educationInfos(educationInfoDao.getEducationInfoByResumeId(resume.getId()))
                            .workExperienceInfos(workExperienceInfoDao.getWorkExperienceByResumeId(resume.getId()))
                            .build()
            );
        }
        return respondedResumeDtos;
    }

    @Override
    public List<VacancyDto> getVacanciesByQuery(String query) {
        return getVacancyDtos(vacancyDao.getVacanciesByQuery(query));
    }

    @Override
    @SneakyThrows
    public void respondToVacancy(RespondedApplicantDto respondedApplicantDto) {
        if (!resumeDao.idIsExists(respondedApplicantDto.getResumeId())) {
            throw new ResumeException("Резюме не существует!");
        }
        if (!vacancyDao.isExists(respondedApplicantDto.getVacancyId())) {
            throw new VacancyException("Вакансии не существует!");
        }
        if (respondedApplicantDao.isExists(respondedApplicantDto.getResumeId(), respondedApplicantDto.getVacancyId())) {
            throw new VacancyException("Соискатель уже откликался на эту вакансию!");
        }
        respondedApplicantDao.respondToVacancy(respondedApplicantDto.getResumeId(), respondedApplicantDto.getVacancyId());
    }

    @Override
    public List<ProfileAndVacancyDto> getVacanciesByEmployerName(String employer) {
        List<User> users = new ArrayList<>(userDao.getUsersByName(employer));
        List<ProfileAndVacancyDto> vacAndUsers = new ArrayList<>();
        for (User currUsr : users) {
            vacAndUsers.add(ProfileAndVacancyDto.builder()
                    .name(currUsr.getName())
                    .surname(currUsr.getSurname())
                    .vacancyDtos(getVacancyDtos(vacancyDao.getVacanciesByAuthorId(currUsr.getId())))
                    .build());
        }
        return vacAndUsers;
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
                .createdTime(e.getCreatedTime())
                .updateTime(e.getUpdateTime())
                .build()));
        return vacancyDtos;
    }

    private VacancyDto getVacancyDto(Vacancy vacancy) {
        return VacancyDto.builder()
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .categoryId(vacancy.getCategoryId())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .createdTime(vacancy.getCreatedTime())
                .updateTime(vacancy.getUpdateTime())
                .build();
    }
}
