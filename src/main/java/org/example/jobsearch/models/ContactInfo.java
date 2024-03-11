package org.example.jobsearch.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo {
    private Long id;
    private Long typeId;
    private Long resumeId;
    private String value;
}
