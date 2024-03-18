package org.example.jobsearch.service;

import org.example.jobsearch.dto.EducationInfoDto;
import org.example.jobsearch.models.EducationInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationInfoService {
    List<EducationInfoDto> getDtos(List<EducationInfo> list);

    boolean isValid(List<EducationInfoDto> educationInfos, Long applicantId);
}
