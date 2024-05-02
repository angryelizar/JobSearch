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
    private boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Vacancy> vacancies;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicant")
    private List<Resume> resumes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "toFrom")
    private List<Message> messageListFrom;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fromTo")
    private List<Message> messageListTo;
}
