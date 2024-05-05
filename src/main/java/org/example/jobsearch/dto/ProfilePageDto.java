package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePageDto {
    private UserDto user;
    private Page<ProfilePageResumeDto> resumes;
    private Page<ProfilePageVacancyDto> vacancies;
}
