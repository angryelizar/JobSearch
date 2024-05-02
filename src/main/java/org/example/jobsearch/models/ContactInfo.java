package org.example.jobsearch.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CONTACTS_INFO")
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private ContactType contactType;
    @ManyToOne
    @JoinColumn(name = "RESUME_ID")
    private Resume resume;
    private String content;
}
