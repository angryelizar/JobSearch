package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.service.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping("resumes")
    public ResponseEntity<List<ResumeDto>> getResumes(){
        return ResponseEntity.ok(resumeService.getResumes());
    }

    @GetMapping("resumes/category/{id}")
    public ResponseEntity<?> getResumesByCategoryId(@PathVariable int id){
        try {
            List<ResumeDto> resumes = resumeService.getResumesByCategoryId(id);
            return ResponseEntity.ok().body(resumes);
        } catch (ResumeNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
