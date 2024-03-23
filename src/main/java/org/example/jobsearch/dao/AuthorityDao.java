package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorityDao {
    private final JdbcTemplate template;

    public void add(Long userId, Long authorityId) {
        String sql = """
                insert into ROLES (USER_ID, AUTHORITY_ID)
                VALUES (?, ?)
                """;
        template.update(sql, userId, authorityId);
    }

    public Long getAuthorityIdByAccountType(String authority) {
        String sql = """
                select id from AUTHORITIES
                where AUTHORITY = ?
                """;
        return template.queryForObject(sql, Long.class);
    }
}
