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

    public List<RespondApplicant> getRespondedApplicantsByVacancyId(Long id) {
        String sql = """
                select * from RESPONDED_APPLICANTS
                where VACANCY_ID = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(RespondApplicant.class), id);
    }

    public void deleteRespondedApplicantsByResumeId(Long id) {
        String sql = """
                delete from RESPONDED_APPLICANTS
                where RESUME_ID = ?;
                """;
        template.update(sql, id);
    }

    public boolean isExists(Long resumeId, Long vacancyId) {
        String sql = "SELECT COUNT(*) FROM RESPONDED_APPLICANTS WHERE RESUME_ID = ? and VACANCY_ID = ?";
        Long count = template.queryForObject(sql, Long.class, resumeId, vacancyId);
        return count > 0;
    }

    public void respondToVacancy(Long resumeId, Long vacancyId) {
        String sql = """
                insert into RESPONDED_APPLICANTS
                ( RESUME_ID, VACANCY_ID, CONFIRMATION)
                values ( ?, ?, false )
                """;
        template.update(sql, resumeId, vacancyId);
    }
}
