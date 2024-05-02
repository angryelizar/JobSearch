package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.RespondedApplicantDao;
import org.example.jobsearch.dao.ResumeDao;
import org.example.jobsearch.dao.VacancyDao;
import org.example.jobsearch.dto.ResponseApplicantDto;
import org.example.jobsearch.dto.ResponseEmployerDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.UserException;
import org.example.jobsearch.exceptions.VacancyException;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.example.jobsearch.repositories.UserRepository;
import org.example.jobsearch.service.RespondedApplicantsService;
import org.example.jobsearch.service.ResumeService;
import org.example.jobsearch.service.UserService;
import org.example.jobsearch.service.VacancyService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RespondedApplicantsServiceImpl implements RespondedApplicantsService {
    private final RespondedApplicantDao respondedApplicantDao;
    private final UserService userService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final ResumeDao resumeDao;
    private final VacancyDao vacancyDao;
    private final UserRepository userRepository;

    @Override
    @SneakyThrows
    public List<ResponseApplicantDto> getApplicantResponsesByUser(Authentication authentication) {
        User user = userService.getFullUserByEmail(authentication.getName());
        List<RespondApplicant> respondApplicants = new ArrayList<>(respondedApplicantDao.getByApplicantEmail(user.getEmail()));
        return getApplicantResponseDtos(respondApplicants);
    }

    @Override
    @SneakyThrows
    public Integer getApprovedResponsesNumber(Authentication authentication) {
        User user = userService.getFullUserByEmail(authentication.getName());
        return respondedApplicantDao.getApprovedResponsesNumber(user.getId());
    }

    @Override
    @SneakyThrows
    public List<ResponseEmployerDto> getEmployerResponsesByUser(Authentication authentication) {
        User user = userService.getFullUserByEmail(authentication.getName());
        List<RespondApplicant> list = respondedApplicantDao.getByEmployerEmail(user.getEmail());
        return getEmployerResponseDtos(list);
    }

    @Override
    @SneakyThrows
    public void acceptResponse(Long resume, Long vacancy, Authentication authentication) {
        if (!resumeDao.idIsExists(resume)){
            throw new ResumeException("Резюме не существует");
        }
        if (!vacancyDao.isExists(vacancy)){
            throw new VacancyException("Вакансии не существует");
        }
        if (userService.isApplicant(authentication.getName())){
            throw new UserException("Пользователь не работодатель!");
        }
        respondedApplicantDao.acceptResponse(resume, vacancy);
    }

    @Override
    @SneakyThrows
    public void denyResponse(Long resume, Long vacancy, Authentication authentication) {
        if (!resumeDao.idIsExists(resume)){
            throw new ResumeException("Резюме не существует");
        }
        if (!vacancyDao.isExists(vacancy)){
            throw new VacancyException("Вакансии не существует");
        }
        if (userService.isApplicant(authentication.getName())){
            throw new UserException("Пользователь не работодатель!");
        }
        respondedApplicantDao.denyResponse(resume, vacancy);
    }

    @Override
    public String getEmployerNameById(Long id) {
        RespondApplicant rs = respondedApplicantDao.getById(id);
        Vacancy vacancy = vacancyDao.getVacancyById(rs.getVacancyId()).get();
        User author = userRepository.findById(vacancy.getAuthorId()).get();
        return author.getName() + " " + author.getSurname();
    }

    @Override
    public Integer getCountOfVacancies(Long id) {
        RespondApplicant rs = respondedApplicantDao.getById(id);
        Vacancy vacancy = vacancyDao.getVacancyById(rs.getVacancyId()).get();
        Long authorId = vacancy.getAuthorId();
        return vacancyDao.getCountByAuthorId(authorId);
    }

    @Override
    public String getApplicantNameById(Long id) {
        RespondApplicant rs = respondedApplicantDao.getById(id);
        Resume resume = resumeDao.getResumeById(rs.getResumeId()).get();
        User author = userRepository.findById(resume.getApplicantId()).get();
        return author.getName() + " " + author.getSurname();
    }

    @Override
    public Integer getCountOfResumes(Long id) {
        RespondApplicant rs = respondedApplicantDao.getById(id);
        Resume resume = resumeDao.getResumeById(rs.getResumeId()).get();
        Long authorId = resume.getApplicantId();
        return resumeDao.getCountByAuthorId(authorId);
    }

    @Override
    public Long getRecipientId(Long respondedApplicantId, Long authorId) {
        RespondApplicant rs = respondedApplicantDao.getById(respondedApplicantId);
        Resume resume = resumeDao.getResumeById(rs.getResumeId()).get();
        Vacancy vacancy = vacancyDao.getVacancyById(rs.getVacancyId()).get();
        if (Objects.equals(authorId, resume.getApplicantId())){
            return vacancy.getAuthorId();
        } else {
            return resume.getApplicantId();
        }
    }

    private List<ResponseEmployerDto> getEmployerResponseDtos(List<RespondApplicant> list) {
        List<ResponseEmployerDto> employerDtoList = new ArrayList<>();
        for (RespondApplicant cur : list) {
            User author = resumeService.getAuthorByResumeId(cur.getResumeId());
            employerDtoList.add(ResponseEmployerDto.builder()
                    .vacancyName(vacancyService.getNameById(cur.getVacancyId()))
                    .vacancyId(cur.getVacancyId())
                    .resumeId(cur.getResumeId())
                    .resumeName(resumeService.getResumeNameById(cur.getResumeId()))
                    .applicantId(author.getId())
                    .applicantName(author.getName() + " " + author.getSurname())
                            .status(getEmployerStatusByBoolean(cur.getConfirmation()))
                    .build());
        }
        return employerDtoList;
    }

    public List<ResponseApplicantDto> getApplicantResponseDtos(List<RespondApplicant> list) {
        List<ResponseApplicantDto> responses = new ArrayList<>();
        for (RespondApplicant respondApplicant : list) {
            responses.add(ResponseApplicantDto.builder()
                    .resumedId(respondApplicant.getResumeId())
                    .resumeName(resumeService.getResumeNameById(respondApplicant.getResumeId()))
                    .vacancyName(vacancyService.getNameById(respondApplicant.getVacancyId()))
                    .vacancyId(respondApplicant.getVacancyId())
                    .status(getApplicantStatusByBoolean(respondApplicant.getConfirmation()))
                    .build());
        }
        return responses;
    }

    private String getApplicantStatusByBoolean(Boolean confirmation) {
        if (confirmation == null) {
            return "В обработке";
        } else if (confirmation) {
            return "Одобрено";
        } else {
            return "Отклонено";
        }
    }

    private String getEmployerStatusByBoolean(Boolean confirmation) {
        if (confirmation == null) {
            return "Новый отклик";
        } else if (confirmation) {
            return "Одобрено";
        } else {
            return "Отклонено";
        }
    }
}
