package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePageResumeDto {
    private Long id;
    private String name;
    private Double salary;
    private String updateDate;
}
