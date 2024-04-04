package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dao.WorkExperienceInfoDao;
import org.example.jobsearch.dto.PageWorkExperienceInfo;
import org.example.jobsearch.dto.WorkExperienceInfoDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.models.WorkExperienceInfo;
import org.example.jobsearch.service.WorkExperienceInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {
    private final UserDao userDao;
    private final WorkExperienceInfoDao workExperienceInfoDao;

    public boolean isValid(List<WorkExperienceInfoDto> list, Long userId) {
        try {
            for (WorkExperienceInfoDto curWeId : list) {
                if (curWeId.getCompanyName().isEmpty() || curWeId.getCompanyName().isBlank()) {
                    throw new ResumeException("Имя компании не может быть пустым!");
                }
                if (curWeId.getPosition().isEmpty() || curWeId.getPosition().isBlank()) {
                    throw new ResumeException("Поле с должностью не может быть пустым!");
                }
                if (curWeId.getResponsibilities().isEmpty() || curWeId.getResponsibilities().isBlank()) {
                    throw new ResumeException("Поле с обязанностями не может быть пустым!");
                }
                Integer age = userDao.getUserAge(userId);
                if (age != null) {
                    if (curWeId.getYears() > age) {
                        throw new ResumeException("Опыт работы не может быть больше возраста соискателя!");
                    }
                }
            }
        } catch (ResumeException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public List<WorkExperienceInfoDto> getDtos(List<WorkExperienceInfo> list) {
        List<WorkExperienceInfoDto> dtos = new ArrayList<>();
        list.forEach(e -> dtos.add(
                WorkExperienceInfoDto.builder()
                        .years(e.getYears())
                        .companyName(e.getCompanyName())
                        .position(e.getPosition())
                        .responsibilities(e.getResponsibilities())
                        .build()
        ));
        return dtos;
    }

    @Override
    public List<PageWorkExperienceInfo> getPageWorkExperienceByResumeId(Long id) {
        List<WorkExperienceInfo> workExperienceInfos = workExperienceInfoDao.getWorkExperienceByResumeId(id);
        List<PageWorkExperienceInfo> pageWorkExperienceInfos = new ArrayList<>();
        for (WorkExperienceInfo currInf : workExperienceInfos) {
            pageWorkExperienceInfos.add(
                    PageWorkExperienceInfo.builder()
                            .name(currInf.getCompanyName())
                            .years(currInf.getYears())
                            .position(currInf.getPosition())
                            .responsibilites(currInf.getResponsibilities())
                            .build()
            );
        }
        return pageWorkExperienceInfos;
    }
}
