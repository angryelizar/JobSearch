package org.example.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDto {
    @NotBlank(message = "Название вакансии не может быть пустым")
    @Size(min = 20, max = 100, message = "Размер названия вакансии - от 20 до 100 символов")
    private String name;
    @NotBlank(message = "Описание вакансии не может быть пустым")
    @Size(min = 100, max = 2000, message = "Размер описания вакансии - от 100 до 2000 символов")
    private String description;
    @NotNull(message = "Категория не может быть пустой")
    private Long categoryId;
    @NotNull(message = "Зарплата не может быть не указана")
    private Double salary;
    private Integer expFrom;
    private Integer expTo;
    @NotNull(message = "Укажите активная ли вакансия или нет")
    private Boolean isActive;
//    @NotNull(message = "Укажите автора")
//    private Long authorId;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;
}
