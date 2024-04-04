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
public class ProfilePageDTO {
    private UserDto user;
    private List<ProfilePageResumeDto> resumes;
    private List<ProfilePageVacancyDto> vacancies;
}
