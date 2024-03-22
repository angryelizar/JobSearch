package org.example.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespondedApplicantDto {
    @NotNull(message = "Поле с ID резюме не может быть пустым")
    private Long resumeId;
    @NotNull(message = "Поле с ID вакансии не может быть пустым")
    private Long vacancyId;
}
