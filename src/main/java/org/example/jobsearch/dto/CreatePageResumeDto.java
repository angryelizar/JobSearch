package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePageResumeDto {
    private String name;
    private Long categoryId;
    private Double salary;
    private Boolean isActive;
    private List<WorkExperienceInfoDto> workExperienceInfos;
    private List<EducationInfoDto> educationInfos;
}
