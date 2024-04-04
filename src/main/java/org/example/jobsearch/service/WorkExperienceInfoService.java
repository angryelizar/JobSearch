package org.example.jobsearch.service;

import org.example.jobsearch.dto.PageWorkExperienceInfo;
import org.example.jobsearch.dto.WorkExperienceInfoDto;
import org.example.jobsearch.models.WorkExperienceInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkExperienceInfoService {
    boolean isValid(List<WorkExperienceInfoDto> list, Long userId);

    List<WorkExperienceInfoDto> getDtos(List<WorkExperienceInfo> list);

    List<PageWorkExperienceInfo> getPageWorkExperienceByResumeId(Long id);
}
