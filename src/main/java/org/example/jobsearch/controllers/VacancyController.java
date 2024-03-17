package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.RespondedResumeDto;
import org.example.jobsearch.dto.UpdateVacancyDto;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.dto.VacancyDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.exceptions.VacancyException;
import org.example.jobsearch.exceptions.VacancyNotFoundException;
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


    @PostMapping
    public HttpStatus createVacancy(@RequestBody VacancyDto vacancyDto){
        try {
            vacancyService.createVacancy(vacancyDto);
            return HttpStatus.CREATED;
        } catch (VacancyException e){
            log.info(e.getMessage());
            return HttpStatus.NO_CONTENT;
        }
    }

    @PostMapping("{id}")
    public HttpStatus editVacancy(@PathVariable int id, @RequestBody UpdateVacancyDto updateVacancyDto){
        try {
            vacancyService.editVacancy(id, updateVacancyDto);
            return HttpStatus.ACCEPTED;
        } catch (VacancyException e){
            log.info(e.getMessage());
            return HttpStatus.NO_CONTENT;
        }
    }

    @DeleteMapping("{id}")
    public HttpStatus deleteVacancyById(@PathVariable int id){
        vacancyService.deleteVacancyById(id);
        return HttpStatus.ACCEPTED;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getVacancyById (@PathVariable int id){
        try {
            return ResponseEntity.ok(vacancyService.getVacancyById(id));
        } catch (VacancyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("applicant/{id}")
    public ResponseEntity<?> getVacanciesByApplicantId(@PathVariable int id){
        try {
            List<VacancyDto> vacancyDtos = vacancyService.getVacanciesByApplicantId(id);
            return ResponseEntity.ok().body(vacancyDtos);
        } catch (VacancyNotFoundException | ResumeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("category/{id}")
    public ResponseEntity<?> getVacanciesByCategoryId(@PathVariable int id){
        try {
            List<VacancyDto> vacancyDtos = vacancyService.getVacanciesByCategoryId(id);
            return ResponseEntity.ok().body(vacancyDtos);
        } catch (VacancyNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("{id}/responded-applicants")
    public ResponseEntity<List<RespondedResumeDto>> getRespondedResumesByVacancyId (@PathVariable int id){
        return ResponseEntity.ok(vacancyService.getRespondedResumesByVacancyId(id));
    }

    @GetMapping("{id}/users")
    public ResponseEntity<?> getApplicantsByVacancyId(@PathVariable int id){
        try {
            List<UserDto> userDtos = vacancyService.getApplicantsByVacancyId(id);
            return ResponseEntity.ok().body(userDtos);
        } catch (UserNotFoundException | ResumeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<VacancyDto>> getVacancies(){
        return ResponseEntity.ok(vacancyService.getVacancies());
    }
}
