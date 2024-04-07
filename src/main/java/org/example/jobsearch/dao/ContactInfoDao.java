package org.example.jobsearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.ContactInfo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContactInfoDao {
    private final JdbcTemplate template;

    public List<ContactInfo> getContactInfosByResumeId(Long id) {
        String sql = """
                SELECT * FROM CONTACTS_INFO
                WHERE RESUME_ID = ?
                """;
        return template.query(sql, new BeanPropertyRowMapper<>(ContactInfo.class), id);
    }

    public String getContactInfoType(Long id) {
        String sql = """
                SELECT TYPE FROM CONTACT_TYPES
                WHERE ID = ?
                """;
        return template.queryForObject(sql, String.class, id);
    }

    public Long getContactInfoIdByType(String type){
        String sql = """
                SELECT ID FROM CONTACT_TYPES
                WHERE TYPE = ?
                """;
        return template.queryForObject(sql, Long.class, type);
    }

    public void addContactInfo(ContactInfo contactInfo) {
        String sql = """
                insert into CONTACTS_INFO (TYPE_ID, RESUME_ID, CONTENT) values (?, ?, ?);
                """;
        template.update(sql, contactInfo.getTypeId(), contactInfo.getResumeId(), contactInfo.getContent());
    }
}
