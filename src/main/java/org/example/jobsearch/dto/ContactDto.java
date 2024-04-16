package org.example.jobsearch.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDto {
    private String name;
    private String vacancyName;
    private Long respondedApplicantId;
}
