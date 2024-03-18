package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.*;
import org.example.jobsearch.dto.ProfileAndResumesDto;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.dto.UpdateResumeDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.models.EducationInfo;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.WorkExperienceInfo;
import org.example.jobsearch.service.EducationInfoService;
import org.example.jobsearch.service.ResumeService;
import org.example.jobsearch.service.WorkExperienceInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    private final UserDao userDao;
    private final WorkExperienceInfoDao workExperienceInfoDao;
    private final EducationInfoDao educationInfoDao;
    private final CategoryDao categoryDao;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final EducationInfoService educationInfoService;
    private final RespondedApplicantDao respondedApplicantDao;

    @Override
    public List<ResumeDto> getResumes() {
        List<Resume> resumes = resumeDao.getResumes();
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(int id) throws ResumeNotFoundException {
        List<Resume> resumes = resumeDao.getResumesByCategoryId(id);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Резюме в категории нет или категории не существует");
        }
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByUserId(int id) throws ResumeNotFoundException {
        List<Resume> resumes = resumeDao.getResumesByUserId(id);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Пользователь с этим ID либо не публиковал резюме - либо его нет :(");
        }
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByName(String query) {
        List<Resume> resumes = resumeDao.getResumesByName(query);
        return getResumeDtos(resumes);
    }

    @Override
    public List<ProfileAndResumesDto> getResumesByApplicantName(String user) throws ResumeNotFoundException {
        List<User> users = new ArrayList<>(userDao.getUsersByName(user));
        List<ProfileAndResumesDto> resAndUsers = new ArrayList<>();
        for (User currUsr : users) {
            resAndUsers.add(
                    ProfileAndResumesDto.builder()
                            .name(currUsr.getName())
                            .surname(currUsr.getSurname())
                            .age(currUsr.getAge())
                            .email(currUsr.getEmail())
                            .resumeDtos(getResumesByUserId(Math.toIntExact(currUsr.getId())))
                            .build()
            );
        }
        return resAndUsers;
    }

    @Override
    public void createResume(ResumeDto resumeDto) {
        try {
            if (userDao.userIsEmployer(resumeDto.getApplicantId()) || !userDao.idIsExists(resumeDto.getApplicantId())) {
                throw new ResumeException("Пользователь либо работодатель, либо его не существует!");
            }
            if (Boolean.FALSE.equals(categoryDao.isExists(resumeDto.getCategoryId()))) {
                throw new ResumeException("Выбранной вами категории не существует");
            }
            if (resumeDto.getSalary() <= 0) {
                throw new ResumeException("Зарплата не может быть меньше или равна нулю");
            }
        } catch (ResumeException e) {
            log.error(e.getMessage());
        }
        boolean educationIsValid = true;
        boolean workExperienceIsValid = true;
        if (!resumeDto.getWorkExperienceInfos().isEmpty()) {
            workExperienceIsValid = workExperienceInfoService.isValid(resumeDto.getWorkExperienceInfos(), resumeDto.getApplicantId());
        }
        if (!resumeDto.getEducationInfos().isEmpty()) {
            educationIsValid = educationInfoService.isValid(resumeDto.getEducationInfos(), resumeDto.getApplicantId());
        }
        Long resumeId = resumeDao.createResume(Resume.builder()
                .applicantId(resumeDto.getApplicantId())
                .name(resumeDto.getName())
                .categoryId(resumeDto.getCategoryId())
                .salary(resumeDto.getSalary())
                .isActive(resumeDto.getIsActive())
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
        if (educationIsValid){
            resumeDto.getEducationInfos().forEach(e -> educationInfoDao.createEducationInfo(
                    EducationInfo.builder()
                            .resumeId(resumeId)
                            .institution(e.getInstitution())
                            .program(e.getProgram())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .degree(e.getDegree())
                            .build()
            ));
        }
        if (workExperienceIsValid){
            resumeDto.getWorkExperienceInfos().forEach(e -> workExperienceInfoDao.createWorkExperienceInfo(
                    WorkExperienceInfo.builder()
                            .resumeId(resumeId)
                            .years(e.getYears())
                            .companyName(e.getCompanyName())
                            .position(e.getPosition())
                            .responsibilities(e.getResponsibilities())
                            .build()
            ));
        }
    }

    @Override
    public void editResume(Long id, UpdateResumeDto updateResumeDto) throws ResumeException {
        if (Boolean.FALSE.equals(categoryDao.isExists(updateResumeDto.getCategoryId()))) {
            throw new ResumeException("Выбранной вами категории не существует");
        }
        if (updateResumeDto.getSalary() <= 0) {
            throw new ResumeException("Зарплата не может быть меньше или равна нулю");
        }
        boolean educationIsValid = true;
        boolean workExperienceIsValid = true;
        if (!updateResumeDto.getWorkExperienceInfos().isEmpty()) {
            workExperienceIsValid = workExperienceInfoService.isValid(updateResumeDto.getWorkExperienceInfos(), id);
        }
        if (!updateResumeDto.getEducationInfos().isEmpty()) {
            educationIsValid = educationInfoService.isValid(updateResumeDto.getEducationInfos(), id);
        }
        resumeDao.editResume(Resume.builder()
                        .name(updateResumeDto.getName())
                        .categoryId(updateResumeDto.getCategoryId())
                        .salary(updateResumeDto.getSalary())
                        .isActive(updateResumeDto.getIsActive())
                        .updateTime(LocalDateTime.now())
                .build(), id);
        if (educationIsValid){
            updateResumeDto.getEducationInfos().forEach(e -> educationInfoDao.editEducationInfo(
                    EducationInfo.builder()
                            .institution(e.getInstitution())
                            .program(e.getProgram())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .degree(e.getDegree())
                            .build(), id
            ));
        }
        if (workExperienceIsValid){
            updateResumeDto.getWorkExperienceInfos().forEach(e -> workExperienceInfoDao.editWorkExperienceInfo(
                    WorkExperienceInfo.builder()
                            .years(e.getYears())
                            .companyName(e.getCompanyName())
                            .position(e.getPosition())
                            .responsibilities(e.getResponsibilities())
                            .build(), id
            ));
        }
    }

    @Override
    public void deleteResumeById(int id) {
        workExperienceInfoDao.deleteWorkExperienceInfoByResumeId(id);
        educationInfoDao.deleteEducationInfoByResumeId(id);
        respondedApplicantDao.deleteRespondedApplicantsByResumeId(id);
        resumeDao.deleteResumeById(id);
    }

    private List<ResumeDto> getResumeDtos(List<Resume> resumes) {
        List<ResumeDto> resumeDtos = new ArrayList<>();
        for (Resume rs : resumes) {
            resumeDtos.add(
                    ResumeDto.builder()
                            .applicantId(rs.getApplicantId())
                            .name(rs.getName())
                            .categoryId(rs.getCategoryId())
                            .salary(rs.getSalary())
                            .isActive(rs.getIsActive())
                            .createdTime(rs.getCreatedTime())
                            .updateTime(rs.getUpdateTime())
                            .educationInfos(educationInfoService.getDtos(educationInfoDao.getEducationInfoByResumeId(rs.getId())))
                            .workExperienceInfos(workExperienceInfoService.getDtos(workExperienceInfoDao.getWorkExperienceByResumeId(rs.getId())))
                            .build()
            );
        }
        return resumeDtos;
    }
}
