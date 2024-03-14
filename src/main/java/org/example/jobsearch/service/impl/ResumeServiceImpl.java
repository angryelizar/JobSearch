package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dao.ResumeDao;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.service.ResumeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    @Override
    public List<ResumeDto> getResumes() {
        List<Resume> resumes = resumeDao.getResumes();
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(int id) throws ResumeNotFoundException {
        List<Resume> resumes = resumeDao.getResumesByCategoryId(id);
        if (resumes.isEmpty()){
            throw new ResumeNotFoundException("Резюме в категории нет или категории не существует");
        }
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByUserId(int id) throws ResumeNotFoundException {
        List<Resume> resumes = resumeDao.getResumesByUserId(id);
        if (resumes.isEmpty()){
            throw new ResumeNotFoundException("Пользователь с этим ID либо не публиковал резюме - либо его нет :(");
        }
        return getResumeDtos(resumes);
    }

    private List<ResumeDto> getResumeDtos(List<Resume> resumes) {
        List<ResumeDto> resumeDtos = new ArrayList<>();
        resumes.forEach(e -> resumeDtos.add(ResumeDto.builder()
                .name(e.getName())
                .applicantId(e.getApplicantId())
                .categoryId(e.getCategoryId())
                .salary(e.getSalary())
                .isActive(e.getIsActive())
                .createdTime(e.getCreatedTime())
                .updateTime(e.getUpdateTime())
                .build()));
        return resumeDtos;
    }
}
