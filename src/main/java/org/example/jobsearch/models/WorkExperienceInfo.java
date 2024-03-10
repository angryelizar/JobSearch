package org.example.jobsearch.models;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperienceInfo {
    private Long id;
    private Long resumeId;
    private Integer years;
    private String companyName;
    private String position;
    private String responsibilities;
}
