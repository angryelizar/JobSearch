package org.example.jobsearch.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    private String name;
    private String surname;
    private Byte age;
    private String email;
    private String phoneNumber;
    private String avatar;
    private String accountType;
}
