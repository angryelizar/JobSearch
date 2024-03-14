package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.Resume;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ResumeDao {
    private final JdbcTemplate template;
    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

    public List<Resume> getResumes() {
        String sql = """
                select * from RESUMES
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Resume.class));
    }

    public List<Resume> getResumesByCategoryId(int id) {
        String sql = """
                select * from RESUMES
                where category_id = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Resume.class), id);
    }

    public List<Resume> getResumesByUserId(int id) {
        String sql = """
                select * from RESUMES
                where applicant_id = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Resume.class), id);
    }

    public Optional<Resume> getResumeById(int id) {
        String sql = """
                select * from RESUMES
                where id = ?
                """;
        return Optional.ofNullable(DataAccessUtils.singleResult(
                template.query(sql, new BeanPropertyRowMapper<>(Resume.class), id)
        ));
    }

    public void deleteResumeById(int id) {
        String sql = """
                delete from RESUMES
                where id = ?
                """;
        template.update(sql, id);
    }

    public void setNameOfResume(String newName, int resumeId) {
        String sql = """
                update RESUMES
                SET NAME = :new_name
                where id = :resume_id
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("new_name", newName);
        params.put("resume_id", resumeId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void setCategoryOfResume(Long newCategory, int resumeId) {
        String sql = """
                update RESUMES
                SET CATEGORY_ID = :new_category
                where id = :resume_id
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("new_category", newCategory);
        params.put("resume_id", resumeId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void setSalaryOfResume(int newSalary, int resumeId) {
        String sql = """
                update RESUMES
                SET SALARY = :new_salary
                where id = :resume_id
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("new_salary", newSalary);
        params.put("resume_id", resumeId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void setIsActiveOfResume(boolean isActive, int resumeId) {
        String sql = """
                update RESUMES
                SET IS_ACTIVE = :is_Active
                where id = :resume_id
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("is_Active", isActive);
        params.put("resume_id", resumeId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void setUpdateTime(LocalDateTime updateTime, int resumeId) {
        String sql = """
                update RESUMES
                SET UPDATE_TIME = :update_time
                where id = :resume_id
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("update_time", updateTime);
        params.put("resume_id", resumeId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public Long createResume(Resume resume) {
        String sql = """
                insert into resumes (APPLICANT_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_TIME, UPDATE_TIME)
                values (?, ?, ?, ?, ?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, resume.getApplicantId());
            ps.setString(2, resume.getName());
            ps.setLong(3, resume.getCategoryId());
            ps.setDouble(4, resume.getSalary());
            ps.setBoolean(5, resume.getIsActive());
            ps.setObject(6, resume.getCreatedTime());
            ps.setObject(7, resume.getUpdateTime());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

}
