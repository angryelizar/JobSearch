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
@Table(name = "RESUMES")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "APPLICANT_ID")
    private User applicant;
    private String name;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    private Double salary;
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;
    @Column(name = "UPDATE_TIME")
    private LocalDateTime updateTime;
}
