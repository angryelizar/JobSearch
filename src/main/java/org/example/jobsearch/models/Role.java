package org.example.jobsearch.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private Long userId;
    private Long authorityId;
}
