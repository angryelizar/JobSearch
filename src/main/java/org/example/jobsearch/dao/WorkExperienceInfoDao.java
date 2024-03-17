package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.WorkExperienceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WorkExperienceInfoDao {
    private final JdbcTemplate template;

    public Long createWorkExperienceInfo(WorkExperienceInfo workExperienceInfo) {
        String sql = """
                insert into WORK_EXPERIENCE_INFO (RESUME_ID, YEARS, COMPANY_NAME, POSITION, RESPONSIBILITIES)
                values(?, ?, ?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, workExperienceInfo.getResumeId());
            ps.setInt(2, workExperienceInfo.getYears());
            ps.setString(3, workExperienceInfo.getCompanyName());
            ps.setString(4, workExperienceInfo.getPosition());
            ps.setString(5, workExperienceInfo.getResponsibilities());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<WorkExperienceInfo> getWorkExperienceByResumeId(Long id) {
        String sql = """
                select * from WORK_EXPERIENCE_INFO
                where RESUME_ID = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(WorkExperienceInfo.class), id);
    }
}
