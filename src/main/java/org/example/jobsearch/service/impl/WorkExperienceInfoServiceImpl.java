package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.PageWorkExperienceInfo;
import org.example.jobsearch.dto.WorkExperienceInfoDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.WorkExperienceInfo;
import org.example.jobsearch.repositories.UserRepository;
import org.example.jobsearch.repositories.WorkExperienceInfoRepository;
import org.example.jobsearch.service.WorkExperienceInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkExperienceInfoServiceImpl implements WorkExperienceInfoService {
    private final WorkExperienceInfoRepository workExperienceInfoRepository;
    private final UserRepository userRepository;

    @SneakyThrows
    public boolean isValid(List<WorkExperienceInfoDto> list, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Not found user with ID " + userId));
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
                Integer age = user.getAge();
                if (age != null && (curWeId.getYears() > age)) {
                        throw new ResumeException("Опыт работы не может быть больше возраста соискателя!");

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
        List<WorkExperienceInfo> workExperienceInfos = workExperienceInfoRepository.findByResumeId(id);
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

    @Override
    @SneakyThrows
    public boolean isValid(WorkExperienceInfoDto workExperienceInfo) {
        Integer years = workExperienceInfo.getYears();
        String companyName = workExperienceInfo.getCompanyName();
        String position = workExperienceInfo.getPosition();
        String responsibilities = workExperienceInfo.getResponsibilities();
        if (years == null || years <= 0 || years > 100) {
            log.error("Невалидный возраст");
            return false;
        }
        if (companyName == null || companyName.isBlank() || companyName.isEmpty() || companyName.length() >= 100) {
            log.error("Невалидное название компании");
            return false;
        }
        if (position == null || position.isBlank() || position.isEmpty() || position.length() >= 100) {
            log.error("Невалидное название позиции");
            return false;
        }
        if (responsibilities == null || responsibilities.isBlank() || responsibilities.isEmpty() || responsibilities.length() >= 100) {
            log.error("Невалидный текст обязанностей");
            return false;
        }
        return true;
    }
}
