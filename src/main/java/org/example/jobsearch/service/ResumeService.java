package org.example.jobsearch.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.models.Resume;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResumeService {
    List<ResumeDto> getResumes();

    List<ResumeDto> getActiveResumes();

    List<ResumeDto> getInActiveResumes();

    List<ResumeDto> getResumesByCategoryId(Long id) throws ResumeNotFoundException;

    List<ResumeDto> getResumesByUserId(Long id) throws ResumeNotFoundException;

    List<Resume> getFullResumesByUserId(Long id);

    List<ResumeDto> getResumesByName(String query);

    List<PageResumeDto> getActivePageResumes();

    List<ProfileAndResumesDto> getResumesByApplicantName(String user) throws ResumeNotFoundException;

    void createResume(Authentication authentication, ResumeDto resumeDto);

    void editResume(Long id, UpdateResumeDto updateResumeDto);

    void deleteResumeById(Long id);

    void update(Long id);

    PageResumeDto getPageResumeById(Long id);

    void addResumeFromForm(CreatePageResumeDto pageResumeDto, HttpServletRequest request, Authentication auth, String telegram, String whatsapp, String telephone, String linkedin, String email);

    List<PageResumeDto> getPageResumeByCategoryId(Long aLong);

    PageResumeDto resumeEditGet(Long id, Authentication auth);

    Long resumeEditPost(UpdatePageResumeDto resumeDto, HttpServletRequest request, Authentication auth);
}
