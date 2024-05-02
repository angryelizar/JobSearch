package org.example.jobsearch.repositories;

import org.example.jobsearch.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
