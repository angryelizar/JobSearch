package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageWorkExperienceInfo {
    private String name;
    private int years;
    private String position;
    private String responsibilites;
}
