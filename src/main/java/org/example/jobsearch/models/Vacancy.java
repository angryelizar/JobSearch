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
public class Vacancy {
    private Long id;
    private String name;
    private String description;
    private Integer categoryId;
    private Integer salary;
    private Byte expFrom;
    private Byte expTo;
    private Boolean isActive;
    private Integer authorId;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;
}
