package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dao.EducationInfoDao;
import org.example.jobsearch.dao.ResumeDao;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dao.WorkExperienceInfoDao;
import org.example.jobsearch.dto.ProfileAndResumesDto;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.service.ResumeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    private final UserDao userDao;
    private final WorkExperienceInfoDao workExperienceInfoDao;
    private final EducationInfoDao educationInfoDao;
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

    @Override
    public List<ResumeDto> getResumesByName(String query) {
        List<Resume>  resumes = resumeDao.getResumesByName(query);
        return getResumeDtos(resumes);
    }

    @Override
    public List<ProfileAndResumesDto> getResumesByApplicantName(String user) throws ResumeNotFoundException {
        List<User> users = new ArrayList<>(userDao.getUsersByName(user));
        List<ProfileAndResumesDto> resAndUsers = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            User currUsr = users.get(i);
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

    private List<ResumeDto> getResumeDtos(List<Resume> resumes) {
        List<ResumeDto> resumeDtos = new ArrayList<>();
        for (int i = 0; i < resumes.size(); i++) {
            Resume rs = resumes.get(i);
            resumeDtos.add(
                    ResumeDto.builder()
                            .applicantId(rs.getApplicantId())
                            .name(rs.getName())
                            .categoryId(rs.getCategoryId())
                            .salary(rs.getSalary())
                            .isActive(rs.getIsActive())
                            .createdTime(rs.getCreatedTime())
                            .updateTime(rs.getUpdateTime())
                            .educationInfos(educationInfoDao.getEducationInfoByResumeId(rs.getId()))
                            .workExperienceInfos(workExperienceInfoDao.getWorkExperienceByResumeId(rs.getId()))
                            .build()
            );
        }
        return resumeDtos;
    }
}
