package org.example.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResumeDto {
    @NotBlank(message = "Название не может быть пустым")
    @NotNull(message = "Название не может быть пустым")
    @Size(min = 10, max = 100, message = "Название должно содержать от 10 до 100 символов")
    private String name;
    @NotNull(message = "Категория не может быть пустой")
    private Long categoryId;
    @NotNull(message = "Зарплата не может быть не указана")
    private Double salary;
    @NotNull(message = "Укажите активное ли резюме или нет")
    private Boolean isActive;
    private LocalDateTime updateTime;
    private List<EducationInfoDto> educationInfos;
    private List<WorkExperienceInfoDto> workExperienceInfos;
}
