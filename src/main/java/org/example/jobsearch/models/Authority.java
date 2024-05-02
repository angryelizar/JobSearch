package org.example.jobsearch.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTHORITIES")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "authority")
    private Role role;
}
