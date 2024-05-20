package org.example.jobsearch.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "{registration.validation.name}")
    private String name;
    private String surname;
    @Min(value = 18, message = "{registration.validation.age}")
    private Integer age;
    @NotBlank(message = "{registration.validation.email.empty}")
    @Email(message = "{registration.validation.email}")
    private String email;
    @Size(min = 4, max = 24, message = "{registration.validation.password.size}")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).+$", message = "{registration.validation.password.pattern}")
    private String password;
    @Pattern(regexp = "\\+996\\d{9}", message = "{registration.validation.phone.pattern}")
    private String phoneNumber;
    private String avatar;
    @Pattern(regexp = "^(Работодатель|Соискатель)$", message = "{registration.validation.role}")
    private String accountType;
    private MultipartFile avatarFile;
}
