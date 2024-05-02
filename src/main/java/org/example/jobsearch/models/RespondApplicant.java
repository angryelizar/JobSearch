package org.example.jobsearch.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RESPONDED_APPLICANTS")
public class RespondApplicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;
    private Boolean confirmation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "respondedApplicants")
    private List<Message> messageList;
}
