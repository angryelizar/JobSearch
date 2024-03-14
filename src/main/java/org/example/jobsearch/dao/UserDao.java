package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate template;

    public Optional<User> getUserById(Long id) {
        String sql = """
                select * from users
                where id = ?
                """;
        return Optional.ofNullable(DataAccessUtils.singleResult
                (template.query(sql, new BeanPropertyRowMapper<>(User.class), id)));
    }

    public Long createUser (User user) {
        String sql = """
                insert into users (name, surname, email, password, account_type)
                values (?, ?, ?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getAccountType());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

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
