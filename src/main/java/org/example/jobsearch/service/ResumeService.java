package org.example.jobsearch.service;

import org.example.jobsearch.dto.ProfileAndResumesDto;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.dto.UpdateResumeDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResumeService {
    List<ResumeDto> getResumes();
    List<ResumeDto> getResumesByCategoryId(int id) throws ResumeNotFoundException;

    List<ResumeDto> getResumesByUserId(int id)throws ResumeNotFoundException;

    List<ResumeDto> getResumesByName(String query);

    List<ProfileAndResumesDto> getResumesByApplicantName(String user) throws ResumeNotFoundException;

    void createResume(ResumeDto resumeDto);

    void editResume(Long id, UpdateResumeDto updateResumeDto) throws ResumeException;
}
