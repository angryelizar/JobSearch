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

    public void setAvatar(Long id, String fileName) {
        String sql = """
                update USERS
                SET AVATAR = ?
                where id = ?
                """;
        template.update(sql, fileName, id);
    }

    public String getUserNameById(Long id) {
        String sql = """
                select name from USERS
                where id = ?;
                """;
        return template.queryForObject(sql, String.class, id);
    }

    public String getSurnameNameById(Long id) {
        String sql = """
                select surname from USERS
                where id = ?;
                """;
        return template.queryForObject(sql, String.class, id);
    }


    public Optional<User> getUserById(Long id) {
        String sql = """
                select * from users
                where id = ?
                """;
        return Optional.ofNullable(DataAccessUtils.singleResult
                (template.query(sql, new BeanPropertyRowMapper<>(User.class), id)));
    }

    public Long createUser(User user) {
        String sql = """
                insert into users (name, surname, age, email, password, phone_number, avatar, account_type)
                values (?, ?, ?, ?, ?, ?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setByte(3, user.getAge());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getPhoneNumber());
            ps.setString(7, user.getAvatar());
            ps.setString(8, user.getAccountType());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void changeNameOfUser(String newName, int id) {
        String sql = """
                update users
                set name = ?
                where id = ?
                """;
        template.update(sql, newName, id);
    }

    public void changeSurnameOfUser(String newSurname, int id) {
        String sql = """
                update users
                set surname = ?
                where id = ?
                """;
        template.update(sql, newSurname, id);
    }

    public void changeAgeOfUser(Byte newAge, int id) {
        String sql = """
                update users
                set age = ?
                where id = ?
                """;
        template.update(sql, newAge, id);
    }

    public void changePhoneOfUser(String newPhone, int id) {
        String sql = """
                update users
                set PHONE_NUMBER = ?
                where id = ?
                """;
        template.update(sql, newPhone, id);
    }

    public void changeEmailOfUser(String newEmail, int id) {
        String sql = """
                update users
                set EMAIL = ?
                where id = ?
                """;
        template.update(sql, newEmail, id);
    }

    public void changePasswordOfUser(String newPassword, int id) {
        String sql = """
                update users
                set PASSWORD = ?
                where id = ?
                """;
        template.update(sql, newPassword, id);
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

    public boolean emailIsExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        int count = template.queryForObject(sql, Integer.class, email);
        return count > 0;
    }

    public boolean phoneIsExists(String phone) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE PHONE_NUMBER = ?";
        int count = template.queryForObject(sql, Integer.class, phone);
        return count > 0;
    }

    public boolean idIsExists(Long id) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE ID = ?";
        int count = template.queryForObject(sql, Integer.class, id);
        return count > 0;
    }

    public boolean userIsEmployer(Long id) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE ID = ? and ACCOUNT_TYPE = ?";
        String type = "Работодатель";
        int count = template.queryForObject(sql, Integer.class, id, type);
        return count > 0;
    }

    public int getUserAge(Long id) {
        String sql = "SELECT AGE FROM USERS WHERE ID = ?";
        return template.queryForObject(sql, Integer.class, id);
    }

    public String getAvatarByUserId(Long id) {
        String sql = """
                SELECT AVATAR FROM USERS
                WHERE ID = ?
                """;
        return template.queryForObject(sql, String.class, id);
    }
}
