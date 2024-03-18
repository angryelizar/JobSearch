package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.jobsearch.models.EducationInfo;
import org.example.jobsearch.models.WorkExperienceInfo;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespondedResumeDto {
    private String applicantName;
    private String applicantSurname;
    private String name;
    private String category;
    private Double salary;
    private Boolean isActive;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;
    private List<EducationInfo> educationInfos;
    private List<WorkExperienceInfo> workExperienceInfos;
}
