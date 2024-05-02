package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.RespondedApplicantDao;
import org.example.jobsearch.dto.ResponseApplicantDto;
import org.example.jobsearch.dto.ResponseEmployerDto;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.UserException;
import org.example.jobsearch.exceptions.VacancyException;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.example.jobsearch.repositories.ResumeRepository;
import org.example.jobsearch.repositories.UserRepository;
import org.example.jobsearch.repositories.VacancyRepository;
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
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;
    private final ResumeRepository resumeRepository;

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
        if (!resumeRepository.existsById(resume)){
            throw new ResumeException("Резюме не существует");
        }
        if (!vacancyRepository.existsById(vacancy)){
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
        if (!resumeRepository.existsById(resume)){
            throw new ResumeException("Резюме не существует");
        }
        if (!vacancyRepository.existsById(vacancy)){
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
        Vacancy vacancy = vacancyRepository.findById(rs.getVacancyId()).get();
        User author = vacancy.getAuthor();
        return author.getName() + " " + author.getSurname();
    }

    @Override
    public Integer getCountOfVacancies(Long id) {
        RespondApplicant rs = respondedApplicantDao.getById(id);
        Vacancy vacancy = vacancyRepository.findById(rs.getVacancyId()).get();
        Long authorId = vacancy.getAuthor().getId();
        return vacancyRepository.getCountVacanciesByAuthorId(authorId);
    }

    @Override
    public String getApplicantNameById(Long id) {
        RespondApplicant rs = respondedApplicantDao.getById(id);
        Resume resume = resumeRepository.findById(rs.getResumeId()).get();
        User author = resume.getApplicant();
        return author.getName() + " " + author.getSurname();
    }

    @Override
    public Integer getCountOfResumes(Long id) {
        RespondApplicant rs = respondedApplicantDao.getById(id);
        Resume resume = resumeRepository.findById(rs.getResumeId()).get();
        Long authorId = resume.getApplicant().getId();
        return resumeRepository.countByApplicantId(authorId);
    }

    @Override
    public Long getRecipientId(Long respondedApplicantId, Long authorId) {
        RespondApplicant rs = respondedApplicantDao.getById(respondedApplicantId);
        Resume resume = resumeRepository.findById(rs.getResumeId()).get();
        Vacancy vacancy = vacancyRepository.findById(rs.getVacancyId()).get();
        if (Objects.equals(authorId, resume.getApplicant().getId())){
            return vacancy.getAuthor().getId();
        } else {
            return resume.getApplicant().getId();
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
