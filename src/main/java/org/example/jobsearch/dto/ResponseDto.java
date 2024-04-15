package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private String resumeName;
    private Long resumedId;
    private String vacancyName;
    private Long vacancyId;
    private String status;
}
