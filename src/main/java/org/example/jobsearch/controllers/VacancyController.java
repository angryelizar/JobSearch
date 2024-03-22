package org.example.jobsearch.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.service.VacancyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vacancies")
@RequiredArgsConstructor
@Slf4j
public class VacancyController {
    private final VacancyService vacancyService;


    @GetMapping("search/employers")
    public ResponseEntity<List<ProfileAndVacancyDto>> getResumesByApplicantName(@RequestParam String employer) {
        return ResponseEntity.ok(vacancyService.getVacanciesByEmployerName(employer));
    }

    @GetMapping("search")
    public ResponseEntity<List<VacancyDto>> getVacanciesByQuery(@RequestParam String query) {
        return ResponseEntity.ok(vacancyService.getVacanciesByQuery(query));
    }


    @PostMapping
    public HttpStatus createVacancy(@RequestBody @Valid VacancyDto vacancyDto) {
        vacancyService.createVacancy(vacancyDto);
        return HttpStatus.CREATED;
    }

    @PostMapping("responded-applicant")
    public HttpStatus respondToVacancy(@RequestBody RespondedApplicantDto respondedApplicantDto) {
        try {
            vacancyService.respondToVacancy(respondedApplicantDto);
            return HttpStatus.ACCEPTED;
        } catch (VacancyException | ResumeException e) {
            log.info(e.getMessage());
            return HttpStatus.NOT_FOUND;
        }
    }

    @PostMapping("{id}")
    public HttpStatus editVacancy(@PathVariable Long id, @RequestBody UpdateVacancyDto updateVacancyDto) {
        try {
            vacancyService.editVacancy(id, updateVacancyDto);
            return HttpStatus.ACCEPTED;
        } catch (VacancyException e) {
            log.info(e.getMessage());
            return HttpStatus.NO_CONTENT;
        }
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteVacancyById(@PathVariable Long id) {
        vacancyService.deleteVacancyById(id);
        return HttpStatus.ACCEPTED;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getVacancyById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vacancyService.getVacancyById(id));
        } catch (VacancyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("applicant/{id}")
    public ResponseEntity<?> getVacanciesByApplicantId(@PathVariable Long id) {
        try {
            List<VacancyDto> vacancyDtos = vacancyService.getVacanciesByApplicantId(id);
            return ResponseEntity.ok().body(vacancyDtos);
        } catch (VacancyNotFoundException | ResumeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("category/{id}")
    public ResponseEntity<?> getVacanciesByCategoryId(@PathVariable Long id) {
        try {
            List<VacancyDto> vacancyDtos = vacancyService.getVacanciesByCategoryId(id);
            return ResponseEntity.ok().body(vacancyDtos);
        } catch (VacancyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("{id}/responded-applicants")
    public ResponseEntity<List<RespondedResumeDto>> getRespondedResumesByVacancyId(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getRespondedResumesByVacancyId(id));
    }

    @GetMapping("{id}/users")
    public ResponseEntity<?> getApplicantsByVacancyId(@PathVariable Long id) {
        try {
            List<UserDto> userDtos = vacancyService.getApplicantsByVacancyId(id);
            return ResponseEntity.ok().body(userDtos);
        } catch (UserNotFoundException | ResumeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<VacancyDto>> getVacancies() {
        return ResponseEntity.ok(vacancyService.getVacancies());
    }

    @GetMapping("/active")
    public ResponseEntity<List<VacancyDto>> getActiveVacancies() {
        return ResponseEntity.ok(vacancyService.getActiveVacancies());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<VacancyDto>> getInActiveVacancies() {
        return ResponseEntity.ok(vacancyService.getInActiveVacancies());
    }
}
