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
@Table(name = "CONTACT_TYPES")
public class ContactType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contactType")
    private List<ContactInfo> contactsInfos;
}
