package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.EducationInfoDao;
import org.example.jobsearch.dto.EducationInfoDto;
import org.example.jobsearch.dto.PageEducationInfoDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.models.EducationInfo;
import org.example.jobsearch.models.User;
import org.example.jobsearch.repositories.UserRepository;
import org.example.jobsearch.service.EducationInfoService;
import org.example.jobsearch.util.DateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationInfoServiceImpl implements EducationInfoService {
    private final EducationInfoDao educationInfoDao;
    private final UserRepository userRepository;

    public List<EducationInfoDto> getDtos(List<EducationInfo> list) {
        List<EducationInfoDto> result = new ArrayList<>();
        list.forEach(e -> result.add(
                EducationInfoDto.builder()
                        .institution(e.getInstitution())
                        .program(e.getProgram())
                        .startDate(e.getStartDate())
                        .endDate(e.getEndDate())
                        .degree(e.getDegree())
                        .build()
        ));
        return result;
    }

    @Override
    public boolean isValid(List<EducationInfoDto> educationInfos, Long applicantId) {
        try {
            User author = userRepository.findById(applicantId).get();
            Integer age = author.getAge();
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

    @Override
    public List<PageEducationInfoDto> getPageEducationInfoByResumeId(Long id) {
        List<EducationInfo> educationInfos = educationInfoDao.getEducationInfoByResumeId(id);
        List<PageEducationInfoDto> pageEducationInfoDtos = new ArrayList<>();
        for (int i = 0; i < educationInfos.size(); i++) {
            EducationInfo edInf = educationInfos.get(i);
            pageEducationInfoDtos.add(
                    PageEducationInfoDto
                            .builder()
                            .name(edInf.getInstitution())
                            .dates(DateUtil.getFormattedLocalDate(edInf.getStartDate().atStartOfDay()) + " - " + DateUtil.getFormattedLocalDate(edInf.getEndDate().atStartOfDay()))
                            .program(edInf.getProgram())
                            .degree(edInf.getDegree())
                            .build()
            );
        }
        return pageEducationInfoDtos;
    }

    @Override
    public boolean isValid(EducationInfoDto curDto) {
        String institution = curDto.getInstitution();
        String program = curDto.getProgram();
        String degree = curDto.getDegree();
        LocalDate startDate = curDto.getStartDate();
        LocalDate endDate = curDto.getEndDate();
        if (institution == null || institution.isEmpty() || institution.isBlank() || institution.length() > 100){
            log.error("Невалидное название учебного заведения");
            return false;
        }
        if (program == null || program.isBlank() || program.isEmpty() || program.length() > 100){
            log.error("Невалидное название специализации");
            return false;
        }
        if (degree == null || degree.isBlank() || degree.isEmpty() || degree.length() > 100){
            log.error("Невалидный уровень образования");
            return false;
        }
        if (startDate == null || startDate.isAfter(endDate)){
            log.error("Невалидная дата начала обучения");
            return false;
        }
        if (endDate == null || endDate.isBefore(startDate)){
            log.error("Невалидная дата конца обучения");
            return false;
        }
        return true;
    }


    public boolean isDateRangeValid(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.isBefore(endDate);
    }
}
