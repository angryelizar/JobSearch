package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.models.Resume;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ResumeDao {
    private final JdbcTemplate template;

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
                SET NAME = ?
                where id = ?
                """;
        template.update(sql, newName, resumeId);
    }

    public void setCategoryOfResume(Long newCategory, int resumeId) {
        String sql = """
                update RESUMES
                SET CATEGORY_ID = ?
                where id = ?
                """;
        template.update(sql, newCategory, resumeId);
    }

    public void setSalaryOfResume(int newSalary, int resumeId) {
        String sql = """
                update RESUMES
                SET SALARY = ?
                where id = ?
                """;
        template.update(sql, newSalary, resumeId);
    }

    public void setIsActiveOfResume(boolean isActive, int resumeId) {
        String sql = """
                update RESUMES
                SET IS_ACTIVE = ?
                where id = ?
                """;
        template.update(sql, isActive, resumeId);
    }

    public void setUpdateTime(LocalDateTime updateTime, int resumeId) {
        String sql = """
                update RESUMES
                SET UPDATE_TIME = ?
                where id = ?
                """;
        template.update(sql, updateTime, resumeId);
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

    public List<Resume> getResumesByName(String query) {
        String sql = """
                select * from RESUMES
                where NAME like ?
                """;
        String param = "%" + query + "%";
        return template.query(sql, new BeanPropertyRowMapper<>(Resume.class), param);
    }
}
