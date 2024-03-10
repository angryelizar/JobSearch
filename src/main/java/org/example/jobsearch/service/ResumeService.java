package org.example.jobsearch.service;

import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResumeService {
    List<ResumeDto> getResumes();
    List<ResumeDto> getResumesByCategoryId(int id) throws ResumeNotFoundException;
}
