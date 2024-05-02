package org.example.jobsearch.repositories;

import org.example.jobsearch.models.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EducationInfoRepository extends JpaRepository<EducationInfo, Long> {
    @Query(value = "select * from EDUCATION_INFO where RESUME_ID = :resumeId", nativeQuery = true)
    List<EducationInfo> educationInfoByResumeId(Long resumeId);
}
