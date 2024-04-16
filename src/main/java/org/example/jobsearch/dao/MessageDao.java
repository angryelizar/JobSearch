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

    public void create(Message message) {
        String sql = """
                INSERT INTO MESSAGES (RESPONDED_APPLICANTS, TO_FROM, FROM_TO, CONTENT, DATE_TIME)
                VALUES (?, ?, ?, ?, ?);
                """;
        template.update(sql, message.getRespondApplicantId(), message.getToFrom(), message.getFromTo(), message.getContent(), message.getDateTime());
    }
}
