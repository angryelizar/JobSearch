package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.VacancyDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.Vacancy;
import org.springframework.context.annotation.Bean;
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
        for (int i = 0; i < usersResume.size(); i++) {
            Long resumeId = usersResume.get(i).getId();
            String sql = """
                    select vacancy_id from responded_applicants
                    where resume_id = ?
                    """;
            respondApplicants.addAll(template.query(sql, new BeanPropertyRowMapper<>(RespondApplicant.class), resumeId));
        }
        List<Vacancy> vacancies = new ArrayList<>();
        for (int i = 0; i < respondApplicants.size(); i++) {
            Long vacancyId = respondApplicants.get(i).getVacancyId();
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
}
