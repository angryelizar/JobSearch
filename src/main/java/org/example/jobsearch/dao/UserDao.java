package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate template;

    public List<User> getUsersByName(String name) {
        String sql = """
                select * from users
                where name like ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(User.class), name);
    }

    public List<User> getUsers() {
        String sql = """
                select * from users
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(User.class));
    }
}
