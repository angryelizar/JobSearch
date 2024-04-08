package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDao {
    private final JdbcTemplate template;

    public Boolean isExists(Long id) {
        String sql = "SELECT COUNT(*) FROM CATEGORIES WHERE ID = ?";
        int count = template.queryForObject(sql, Integer.class, id);
        return count > 0;
    }

    public String getCategoryNameById(Long id) {
        String sql = """
                select name from CATEGORIES
                where id = ?
                """;
        return template.queryForObject(sql, String.class, id);
    }

    public List<Category> getCategoriesList() {
        String sql = """
                select * from CATEGORIES;
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }
}
