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
@Table(name = "MESSAGES")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "RESPONDED_APPLICANTS")
    private RespondApplicant respondedApplicants;
    @ManyToOne
    @JoinColumn(name = "TO_FROM")
    private User toFrom;
    @ManyToOne
    @JoinColumn(name = "FROM_TO")
    private User fromTo;
    private String content;
    private LocalDateTime dateTime;
}
