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
    @NotBlank
    private String name;
    private String surname;
    @NotNull(message = "Необходимо указать возраст!")
    private Byte age;
    @NotBlank
    @Email
    private String email;
    @Size(min = 4, max = 24, message = "Длина пароля должна быть больше или равно 4 и не больше 24")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).+$", message = "Пароль должен содержать как минимум одну большую букву и цифру")
    private String password;
    @Size(min = 13, max = 13, message = "Введите номер в формате +996XXXXXXXXX")
    @Pattern(regexp = "\\+996\\d{9}", message = "Введите номер в формате +996XXXXXXXXX")
    private String phoneNumber;
    private String avatar;
    @Pattern(regexp = "^(Работодатель|Соискатель)$", message = "Должно быть 'Работодатель' или 'Соискатель'")
    private String accountType;
    private MultipartFile avatarFile;
}
