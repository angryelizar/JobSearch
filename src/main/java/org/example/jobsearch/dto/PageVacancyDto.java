package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageVacancyDto {
    private Long id;
    private String name;
    private String description;
    private String author;
    private String category;
    private Double salary;
    private Integer expFrom;
    private Integer expTo;
    private String updateTime;
}
