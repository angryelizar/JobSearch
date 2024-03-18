package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.RespondApplicant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RespondedApplicantDao {
    private final JdbcTemplate template;

    public List<RespondApplicant> getRespondedApplicantsByVacancyId(int id) {
        String sql = """
                select * from RESPONDED_APPLICANTS
                where VACANCY_ID = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(RespondApplicant.class), id);
    }

    public void deleteRespondedApplicantsByResumeId(int id) {
        String sql = """
                delete from RESPONDED_APPLICANTS
                where RESUME_ID = ?;
                """;
        template.update(sql, id);
    }
}
