package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.Message;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageDao {
    private final JdbcTemplate template;

    public List<Message> messageGetByRespondedApplicantId(Long id) {
        String sql = """
                SELECT * FROM MESSAGES
                WHERE RESPONDED_APPLICANTS = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Message.class), id);
    }
}
