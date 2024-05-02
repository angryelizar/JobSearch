package org.example.jobsearch.controllers.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.service.VacancyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("restVacancy")
@RequestMapping("api/vacancies")
@RequiredArgsConstructor
@Slf4j
public class VacancyController {
    private final VacancyService vacancyService;

    @GetMapping("/search/employer")
    public ResponseEntity<List<ProfileAndVacancyDto>> getVacanciesByUserName(@RequestParam String name) {
        return ResponseEntity.ok(vacancyService.getVacanciesByEmployerName(name));
    }

    @GetMapping("/search")
    public ResponseEntity<List<VacancyDto>> getVacanciesByQuery(@RequestParam String query) {
        return ResponseEntity.ok(vacancyService.getVacanciesByQuery(query));
    }

    @PostMapping
    public HttpStatus createVacancy(@RequestBody @Valid VacancyDto vacancyDto, Authentication auth) {
        vacancyService.createVacancy(auth, vacancyDto);
        return HttpStatus.CREATED;
    }

    @PostMapping("/responded-applicant")
    public HttpStatus respondToVacancy(@RequestBody @Valid RespondedApplicantDto respondedApplicantDto) {
        vacancyService.respondToVacancy(respondedApplicantDto);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/{id}")
    public HttpStatus editVacancy(@PathVariable Long id, @RequestBody @Valid UpdateVacancyDto updateVacancyDto) {
        vacancyService.editVacancy(id, updateVacancyDto);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/resume")
    public ResponseEntity<?> getResumesForVacancy(@RequestBody ResumesForVacancyDto resumesForVacancyDto){
        return ResponseEntity.ok(vacancyService.getResumesForVacancy(resumesForVacancyDto));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteVacancyById(@PathVariable Long id) {
        vacancyService.deleteVacancyById(id);
        return HttpStatus.ACCEPTED;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVacancyById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(vacancyService.getVacancyById(id));
        } catch (VacancyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getVacanciesByCategoryId(@PathVariable Long id) {
        try {
            List<VacancyDto> vacancyDtos = vacancyService.getVacanciesByCategoryId(id);
            return ResponseEntity.ok().body(vacancyDtos);
        } catch (VacancyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/responded-applicants")
    public ResponseEntity<List<RespondedResumeDto>> getRespondedResumesByVacancyId(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getRespondedResumesByVacancyId(id));
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
