package org.example.jobsearch.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private Long respondApplicantId;
    private String content;
    private LocalDateTime dateTime;
}
