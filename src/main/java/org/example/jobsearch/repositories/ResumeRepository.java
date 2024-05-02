package org.example.jobsearch.repositories;

import org.example.jobsearch.models.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByIsActive(Boolean isActive);

    @Query(value = "select * from RESUMES where category_id = :categoryId", nativeQuery = true)
    List<Resume> findByCategoryId(Long categoryId);

    @Query(value = "select * from RESUMES where APPLICANT_ID = :applicantId", nativeQuery = true)
    List<Resume> findByAuthorId(Long applicantId);

    @Query(value = "select * from RESUMES where NAME like :query", nativeQuery = true)
    List<Resume> findByNameLike(String query);

    @Query(value = "SELECT COUNT(*) FROM RESUMES WHERE APPLICANT_ID = :id", nativeQuery = true)
    Integer countByApplicantId(Long id);
}
