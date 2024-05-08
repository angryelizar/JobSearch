package org.example.jobsearch.repositories;

import org.example.jobsearch.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Query(value = "select * from AUTHORITIES where AUTHORITY = :accountType", nativeQuery = true)
    Authority getAuthorityByAccountType(String accountType);
}
