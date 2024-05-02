package org.example.jobsearch.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLES")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @OneToOne
    @JoinColumn(name = "AUTHORITY_ID")
    private Authority authority;
}
