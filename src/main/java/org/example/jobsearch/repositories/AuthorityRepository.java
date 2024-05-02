package org.example.jobsearch.repositories;

import org.example.jobsearch.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Query(value = "select id from AUTHORITIES where AUTHORITY = :authority", nativeQuery = true)
    Long getAuthorityIdByAccountType(String authority);
}
