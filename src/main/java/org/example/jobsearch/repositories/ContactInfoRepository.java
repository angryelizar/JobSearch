package org.example.jobsearch.repositories;

import org.example.jobsearch.models.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
    @Query(value = "SELECT ID FROM CONTACT_TYPES WHERE TYPE = :query", nativeQuery = true)
    Long findContactTypeIdByQuery (String query);
    @Query(value = "SELECT * FROM CONTACTS_INFO WHERE RESUME_ID = :id", nativeQuery = true)
    List<ContactInfo> getContactInfosByResumeId(Long id);
}
