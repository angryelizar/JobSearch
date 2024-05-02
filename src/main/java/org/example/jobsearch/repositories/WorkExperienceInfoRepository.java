package org.example.jobsearch.repositories;

import org.example.jobsearch.models.WorkExperienceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkExperienceInfoRepository extends JpaRepository<WorkExperienceInfo, Long> {
    @Query(value = "select * from WORK_EXPERIENCE_INFO where RESUME_ID = :resumeId", nativeQuery = true)
    List<WorkExperienceInfo> findByResumeId(Long resumeId);
}
