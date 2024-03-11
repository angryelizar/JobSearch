package org.example.jobsearch.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String surname;
    private Byte age;
    private String email;
    private String password;
    private String phoneNumber;
    private String avatar;
    private String accountType;
}
