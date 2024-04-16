package org.example.jobsearch.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private Long respondApplicantId;
    private Long toFrom;
    private Long fromTo;
    private String content;
    private LocalDateTime dateTime;
}
