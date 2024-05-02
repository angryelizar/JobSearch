package org.example.jobsearch.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VACANCIES")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    private Double salary;
    @Column(name = "EXP_FROM")
    private Integer expFrom;
    @Column(name = "EXP_TO")
    private Integer expTo;
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID")
    private User author;
    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;
    @Column(name = "UPDATE_TIME")
    private LocalDateTime updateTime;
}
