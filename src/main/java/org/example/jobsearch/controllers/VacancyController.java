package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.dto.VacancyDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.exceptions.VacancyNotFoundException;
import org.example.jobsearch.service.VacancyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VacancyController {
    private final VacancyService vacancyService;

    @GetMapping("vacancies/applicant/{id}")
    public ResponseEntity<?> getVacanciesByApplicantId(@PathVariable int id){
        try {
            List<VacancyDto> vacancyDtos = vacancyService.getVacanciesByApplicantId(id);
            return ResponseEntity.ok().body(vacancyDtos);
        } catch (VacancyNotFoundException | ResumeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("vacancies/category/{id}")
    public ResponseEntity<?> getVacanciesByCategoryId(@PathVariable int id){
        try {
            List<VacancyDto> vacancyDtos = vacancyService.getVacanciesByCategoryId(id);
            return ResponseEntity.ok().body(vacancyDtos);
        } catch (VacancyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("vacancies")
    public ResponseEntity<List<VacancyDto>> getVacancies(){
        return ResponseEntity.ok(vacancyService.getVacancies());
    }
}
