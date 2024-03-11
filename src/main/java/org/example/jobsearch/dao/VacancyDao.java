package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VacancyDao {
    private final JdbcTemplate template;
    private final ResumeDao resumeDao;

    public List<Vacancy> getVacanciesByApplicantId(int id) throws ResumeNotFoundException {
        List<Resume> usersResume = resumeDao.getResumesByUserId(id);
        if (usersResume.isEmpty()){
            throw new ResumeNotFoundException("У данного пользователя нет резюме");
        }
        List<RespondApplicant> respondApplicants = new ArrayList<>();
        for (Resume resume : usersResume) {
            Long resumeId = resume.getId();
            String sql = """
                    select vacancy_id from responded_applicants
                    where resume_id = ?
                    """;
            respondApplicants.addAll(template.query(sql, new BeanPropertyRowMapper<>(RespondApplicant.class), resumeId));
        }
        List<Vacancy> vacancies = new ArrayList<>();
        for (RespondApplicant respondApplicant : respondApplicants) {
            Long vacancyId = respondApplicant.getVacancyId();
            String sql = """
                    select * from vacancies
                    where id = ?
                    """;
            vacancies.addAll(template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), vacancyId));
        }
        return vacancies;
    }

    public List<Vacancy> getVacancies() {
        String sql = """
                select * from vacancies
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class));
    }

    public List<Vacancy> getVacanciesByCategoryId(int id) {
        String sql = """
                select * from vacancies
                where category_id = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), id);
    }

    public List<User> getApplicantsByVacancyId(int id) throws ResumeNotFoundException {
        String sql = """
                select resume_id from responded_applicants
                where vacancy_id = ?
                """;
        List<Long> applicantsResumesByVacancyId = template.queryForList(sql, Long.class, id);
        if (applicantsResumesByVacancyId.isEmpty()){
            throw new ResumeNotFoundException("Откликов на эту вакансию не найдено!");
        }
        List<Resume> resumes = new ArrayList<>();
        for (Long resumeId : applicantsResumesByVacancyId) {
            String query = """
                    select * from resumes
                    where id = ?
                    """;
            resumes.addAll(template.query(query, new BeanPropertyRowMapper<>(Resume.class), resumeId));
        }

        List<User> users = new ArrayList<>();
        for (Resume resume : resumes) {
            Long applicantId = resume.getApplicantId();
            String query = """
                    select * from users
                    where id = ?
                    """;
            users.add(template.queryForObject(query, new BeanPropertyRowMapper<>(User.class), applicantId));
        }
        return users;
    }
}
