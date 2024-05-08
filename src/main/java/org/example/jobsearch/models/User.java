package org.example.jobsearch.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User implements UserDetails {
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
    @ManyToOne
    @JoinColumn(name = "AUTHORITY_ID")
    private Authority authority;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Vacancy> vacancies;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicant")
    private List<Resume> resumes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "toFrom")
    private List<Message> messageListFrom;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fromTo")
    private List<Message> messageListTo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authority.getAuthority()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
