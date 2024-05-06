package org.example.jobsearch.repositories;

import org.example.jobsearch.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ROLES (USER_ID, AUTHORITY_ID) values (:userId, :authorityId)", nativeQuery = true)
    void setNewRole(Long userId, Long authorityId);
}
