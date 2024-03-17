package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.exceptions.UserNotFoundException;
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

    @GetMapping()
    public ResponseEntity<List<ResumeDto>> getResumes(){
        return ResponseEntity.ok(resumeService.getResumes());
    }

    @GetMapping("category/{id}")
    public ResponseEntity<?> getResumesByCategoryId(@PathVariable int id){
        try {
            List<ResumeDto> resumes = resumeService.getResumesByCategoryId(id);
            return ResponseEntity.ok().body(resumes);
        } catch (ResumeNotFoundException e){
            log.info("Такой категории нет");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("user/{id}")
    public ResponseEntity<?> getResumesByUserId(@PathVariable int id){
        try {
            List<ResumeDto> resumes = resumeService.getResumesByUserId(id);
            return ResponseEntity.ok().body(resumes);
        } catch (ResumeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
