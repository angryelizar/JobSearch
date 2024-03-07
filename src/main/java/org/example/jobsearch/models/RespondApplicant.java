package org.example.jobsearch.models;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespondApplicant {
    private Long id;
    private Integer resumeId;
    private Integer vacancyId;
    private Boolean confirmation;
}
