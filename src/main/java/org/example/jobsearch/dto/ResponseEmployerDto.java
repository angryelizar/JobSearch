package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEmployerDto {
    private String vacancyName;
    private Long vacancyId;
    private String resumeName;
    private Long resumeId;
    private String applicantName;
    private Long applicantId;
    private String status;
}
