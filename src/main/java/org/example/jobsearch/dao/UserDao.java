package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> getUserByPhone(String phone) {
        String sql = """
                select * from users
                where PHONE_NUMBER = ?
                """;
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        template.query(sql, new BeanPropertyRowMapper<>(User.class),
                                phone)));
    }

    public Optional<User> getUserByEmail(String email) {
        String sql = """
                select * from users
                where EMAIL = ?
                """;
        return Optional.ofNullable(DataAccessUtils.singleResult(
                template.query(sql, new BeanPropertyRowMapper<>(User.class), email)
        ));
    }

    public boolean userIsExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        int count = template.queryForObject(sql, Integer.class, email);
        return count > 0;
    }

}
