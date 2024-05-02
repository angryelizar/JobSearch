package org.example.jobsearch.repositories;

import org.example.jobsearch.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM MESSAGES WHERE RESPONDED_APPLICANTS = :id", nativeQuery = true)
    List<Message> messageGetByRespondedApplicantId(Long id);
}
