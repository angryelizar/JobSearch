package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dto.EducationInfoDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.models.EducationInfo;
import org.example.jobsearch.service.EducationInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationInfoServiceImpl implements EducationInfoService {
    private final UserDao userDao;

    public List<EducationInfoDto> getDtos(List<EducationInfo> list) {
        List<EducationInfoDto> result = new ArrayList<>();
        list.forEach(e -> result.add(
                EducationInfoDto.builder()
                        .institution(e.getInstitution())
                        .program(e.getProgram())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .build()
        ));
        return result;
    }

    @Override
    public boolean isValid(List<EducationInfoDto> educationInfos, Long applicantId) {
        try {
            Integer age = userDao.getUserAge(applicantId);
            for (int i = 0; i < educationInfos.size(); i++) {
                EducationInfoDto dto = educationInfos.get(i);
                if (dto.getProgram().isBlank() || dto.getProgram().isEmpty()) {
                    throw new ResumeException("Поле с описанием учебы не может быть пустым!");
                }
                if (dto.getInstitution().isBlank() || dto.getInstitution().isEmpty()) {
                    throw new ResumeException("Поле с названием учреждения не может быть пустым!");
                }
                if (dto.getDegree().isBlank() || dto.getDegree().isEmpty()) {
                    throw new ResumeException("Поле со специальностью не может быть пустым");
                }
                if (dto.getStartDate() == null) {
                    throw new ResumeException("Поле с датой начала обучения не может быть пустым!");
                }
                if (age != null){
                    int res = dto.getEndDate().getYear() - dto.getStartDate().getYear();
                    if (res > age){
                        throw new ResumeException("Срок обучения не может быть больше возраста автора!");
                    }
                }
            }
        } catch (ResumeException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isDateRangeValid(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.isBefore(endDate);
    }
}
