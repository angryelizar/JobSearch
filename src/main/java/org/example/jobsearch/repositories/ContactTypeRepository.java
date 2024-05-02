package org.example.jobsearch.repositories;

import org.example.jobsearch.models.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactType, Long> {
}
