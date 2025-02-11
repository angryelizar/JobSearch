package org.example.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePageVacancyDto {
    private Long id;
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
    private Boolean isActive;
}
