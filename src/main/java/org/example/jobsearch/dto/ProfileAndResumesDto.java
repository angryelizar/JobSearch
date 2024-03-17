package org.example.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileAndResumesDto {
    private String name;
    private String surname;
    private Byte age;
    private String email;
    List<ResumeDto> resumeDtos;
}
