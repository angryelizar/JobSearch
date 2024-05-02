package org.example.jobsearch.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String password;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    private String avatar;
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;
}
