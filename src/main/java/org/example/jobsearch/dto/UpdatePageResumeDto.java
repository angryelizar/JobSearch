package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePageResumeDto {
    private Long id;
    private String name;
    private Long categoryId;
    private Double salary;
    private Boolean isActive;
}
