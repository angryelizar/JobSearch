package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.*;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.models.*;
import org.example.jobsearch.service.VacancyService;
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

    @Override
    public VacancyDto getVacancyById(int id) throws VacancyNotFoundException {
        if (vacancyDao.getVacancyById(id).isEmpty()) {
            throw new VacancyNotFoundException("Вакансия не найдена");
        }
        return getVacancyDto(vacancyDao.getVacancyById(id).get());
    }

    @Override
    public void editVacancy(int id, UpdateVacancyDto updateVacancyDto) throws VacancyException {
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
        Vacancy vacancy = new Vacancy();
        vacancy.setName(updateVacancyDto.getName());
        vacancy.setDescription(updateVacancyDto.getDescription());
        vacancy.setCategoryId(updateVacancyDto.getCategoryId());
        vacancy.setSalary(updateVacancyDto.getSalary());
        vacancy.setExpFrom(updateVacancyDto.getExpFrom());
        vacancy.setExpTo(updateVacancyDto.getExpTo());
        vacancy.setIsActive(updateVacancyDto.getIsActive());
        vacancy.setUpdateTime(LocalDateTime.now());
        vacancyDao.editVacancy(id, vacancy);
    }

    @Override
    public void deleteVacancyById(int id) {
        vacancyDao.deleteVacancyById(id);
    }

    @Override
    public List<RespondedResumeDto> getRespondedResumesByVacancyId(int id) {
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
    public void respondToVacancy(RespondedApplicantDto respondedApplicantDto) throws ResumeException, VacancyException {
        if (!resumeDao.idIsExists(respondedApplicantDto.getResumeId())) {
            throw new ResumeException("Резюме не существует!");
        }
        if (!vacancyDao.isExists(Math.toIntExact(respondedApplicantDto.getVacancyId()))) {
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
                .authorId(e.getAuthorId())
                .createdTime(e.getCreatedTime())
                .updateTime(e.getUpdateTime())
                .build()));
        return vacancyDtos;
    }

    private VacancyDto getVacancyDto(Vacancy vacancy) {
        VacancyDto vacancyDto = new VacancyDto();
        vacancyDto.setName(vacancy.getName());
        vacancyDto.setDescription(vacancy.getDescription());
        vacancyDto.setCategoryId(vacancy.getCategoryId());
        vacancyDto.setSalary(vacancy.getSalary());
        vacancyDto.setExpFrom(vacancy.getExpFrom());
        vacancyDto.setExpTo(vacancy.getExpTo());
        vacancyDto.setIsActive(vacancy.getIsActive());
        vacancyDto.setAuthorId(vacancy.getAuthorId());
        vacancyDto.setCreatedTime(vacancy.getCreatedTime());
        vacancyDto.setUpdateTime(vacancy.getUpdateTime());
        return vacancyDto;
    }
}
