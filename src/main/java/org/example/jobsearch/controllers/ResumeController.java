package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.ProfileAndResumesDto;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.dto.UpdateResumeDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("resumes")
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping
    public HttpStatus createResume(@RequestBody ResumeDto resumeDto) {
        resumeService.createResume(resumeDto);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("{id}")
    public HttpStatus editResume(@PathVariable Long id, @RequestBody UpdateResumeDto updateResumeDto) {
        try {
            resumeService.editResume(id, updateResumeDto);
            return HttpStatus.ACCEPTED;
        } catch (ResumeException e){
            log.info(e.getMessage());
            return HttpStatus.NO_CONTENT;
        }
    }

    @GetMapping()
    public ResponseEntity<List<ResumeDto>> getResumes() {
        return ResponseEntity.ok(resumeService.getResumes());
    }

    @GetMapping("search")
    public ResponseEntity<List<ResumeDto>> getResumesByName(@RequestParam String name) {
        return ResponseEntity.ok(resumeService.getResumesByName(name));
    }

    @GetMapping("search/applicants")
    public ResponseEntity<List<ProfileAndResumesDto>> getResumesByApplicantName(@RequestParam String user) throws ResumeNotFoundException {
        return ResponseEntity.ok(resumeService.getResumesByApplicantName(user));
    }

    @GetMapping("category/{id}")
    public ResponseEntity<?> getResumesByCategoryId(@PathVariable int id) {
        try {
            List<ResumeDto> resumes = resumeService.getResumesByCategoryId(id);
            return ResponseEntity.ok().body(resumes);
        } catch (ResumeNotFoundException e) {
            log.info("Такой категории нет");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("user/{id}")
    public ResponseEntity<?> getResumesByUserId(@PathVariable int id) {
        try {
            List<ResumeDto> resumes = resumeService.getResumesByUserId(id);
            return ResponseEntity.ok().body(resumes);
        } catch (ResumeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
