package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VacancyDao {
    private final JdbcTemplate template;
    private final ResumeDao resumeDao;


    public List<Vacancy> getVacanciesByApplicantId(Long id) throws ResumeNotFoundException {
        List<Resume> usersResume = resumeDao.getResumesByUserId(id);
        if (usersResume.isEmpty()) {
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

    public List<Vacancy> getVacanciesByCategoryId(Long id) {
        String sql = """
                select * from vacancies
                where category_id = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), id);
    }

    public Optional<Vacancy> getVacancyById(Long id) {
        String sql = """
                select * from vacancies
                where id = ?
                """;
        return Optional.ofNullable(DataAccessUtils.singleResult(
                template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), id)
        ));
    }

    public void deleteVacancyById(Long id) {
        String sql = """
                delete from VACANCIES
                where id = ?
                """;
        template.update(sql, id);
    }

    public Long createVacancy(Vacancy vacancy) {
        String sql = """
                insert into VACANCIES (NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, AUTHOR_ID, CREATED_TIME,
                                       UPDATE_TIME)
                values(?,?,?,?,?,?,?,?,?,?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, vacancy.getName());
            ps.setString(2, vacancy.getDescription());
            ps.setLong(3, vacancy.getCategoryId());
            ps.setDouble(4, vacancy.getSalary());
            ps.setLong(5, vacancy.getExpFrom());
            ps.setLong(6, vacancy.getExpTo());
            ps.setBoolean(7, vacancy.getIsActive());
            ps.setLong(8, vacancy.getAuthorId());
            ps.setObject(9, vacancy.getCreatedTime());
            ps.setObject(10, vacancy.getUpdateTime());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<User> getApplicantsByVacancyId(Long id) throws ResumeNotFoundException {
        String sql = """
                select resume_id from responded_applicants
                where vacancy_id = ?
                """;
        List<Long> applicantsResumesByVacancyId = template.queryForList(sql, Long.class, id);
        if (applicantsResumesByVacancyId.isEmpty()) {
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

    public boolean isExists(Long id) {
        String sql = "SELECT COUNT(*) FROM VACANCIES WHERE ID = ?";
        Long count = template.queryForObject(sql, Long.class, id);
        return count > 0;
    }

    public void editVacancy(Long id, Vacancy vacancy) {
        String sql = """
                update VACANCIES
                SET NAME = ?,
                DESCRIPTION = ?,
                CATEGORY_ID = ?,
                SALARY = ?,
                EXP_FROM = ?,
                EXP_TO = ?,
                IS_ACTIVE = ?,
                UPDATE_TIME = ?
                where id = ?
                """;
        template.update(sql, vacancy.getName(),
                vacancy.getDescription(), vacancy.getCategoryId(),
                vacancy.getSalary(), vacancy.getExpFrom(), vacancy.getExpTo(),
                vacancy.getIsActive(), vacancy.getUpdateTime(), id);
    }

    public List<Vacancy> getVacanciesByQuery(String query) {
        String sql = """
                select * from VACANCIES
                where NAME like ? or DESCRIPTION like ?
                """;
        String searchWord = "%" + query + "%";
        return template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), searchWord, searchWord);
    }

    public List<Vacancy> getVacanciesByAuthorId(Long id) {
        String sql = """
                select * from VACANCIES
                where AUTHOR_ID = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class), id);
    }

    public List<Vacancy> getActiveVacancies() {
        String sql = """
                select * from VACANCIES
                where IS_ACTIVE = true;
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class));
    }

    public List<Vacancy> getInActiveVacancies() {
        String sql = """
                select * from VACANCIES
                where IS_ACTIVE = false;
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Vacancy.class));
    }

    public void update(LocalDateTime date, Long id) {
        String sql = """
                UPDATE VACANCIES
                SET UPDATE_TIME = ?
                WHERE ID = ?
                """;
        template.update(sql, date, id);
    }
}
