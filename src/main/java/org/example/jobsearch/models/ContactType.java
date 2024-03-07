package org.example.jobsearch.models;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactType {
    private Long id;
    private String type;
}
