package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.EducationInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EducationInfoDao {
    private final JdbcTemplate template;

    public Long createEducationInfo(EducationInfo educationInfo) {
        String sql = """
                insert into EDUCATION_INFO (RESUME_ID, INSTITUTION, PROGRAM, START_DATE, END_DATE, DEGREE)
                values (?, ?, ?, ?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, educationInfo.getResumeId());
            ps.setString(2, educationInfo.getInstitution());
            ps.setString(3, educationInfo.getProgram());
            ps.setObject(4, educationInfo.getStartDate());
            ps.setObject(5, educationInfo.getEndDate());
            ps.setString(6, educationInfo.getDegree());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
