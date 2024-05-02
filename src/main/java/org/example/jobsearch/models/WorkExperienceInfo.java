package org.example.jobsearch.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WORK_EXPERIENCE_INFO")
public class WorkExperienceInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
    private Integer years;
    @Column(name = "COMPANY_NAME")
    private String companyName;
    private String position;
    private String responsibilities;
}
