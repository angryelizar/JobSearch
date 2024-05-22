package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.ResponseApplicantDto;
import org.example.jobsearch.dto.ResponseEmployerDto;
import org.example.jobsearch.enums.ResponseStatus;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.UserException;
import org.example.jobsearch.exceptions.VacancyException;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.example.jobsearch.repositories.RespondedApplicantRepository;
import org.example.jobsearch.repositories.ResumeRepository;
import org.example.jobsearch.repositories.VacancyRepository;
import org.example.jobsearch.service.RespondedApplicantsService;
import org.example.jobsearch.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RespondedApplicantsServiceImpl implements RespondedApplicantsService {
    private final UserService userService;
    private final VacancyRepository vacancyRepository;
    private final ResumeRepository resumeRepository;
    private final RespondedApplicantRepository respondedApplicantRepository;

    @Override
    @SneakyThrows
    public List<ResponseApplicantDto> getApplicantResponsesByUser(Authentication authentication) {
        User user = userService.getFullUserByEmail(authentication.getName());
        List<RespondApplicant> respondApplicants = new ArrayList<>(respondedApplicantRepository.findAllByApplicantEmail(user.getEmail()));
        return getApplicantResponseDtos(respondApplicants);
    }

    @Override
    @SneakyThrows
    public Integer getApprovedResponsesNumber(Authentication authentication) {
        User user = userService.getFullUserByEmail(authentication.getName());
        return respondedApplicantRepository.getApprovedResponsesNumber(user.getId());
    }

    @Override
    @SneakyThrows
    public List<ResponseEmployerDto> getEmployerResponsesByUser(Authentication authentication) {
        User user = userService.getFullUserByEmail(authentication.getName());
        List<RespondApplicant> list = respondedApplicantRepository.getByEmployerEmail(user.getEmail());
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
        respondedApplicantRepository.acceptResponse(resume, vacancy);
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
        respondedApplicantRepository.denyResponse(resume, vacancy);
    }

    @Override
    public String getEmployerNameById(Long id) {
        RespondApplicant rs = respondedApplicantRepository.getById(id);
        Vacancy vacancy = rs.getVacancy();
        User author = vacancy.getAuthor();
        return author.getName() + " " + author.getSurname();
    }

    @Override
    public Integer getCountOfVacancies(Long id) {
        RespondApplicant rs = respondedApplicantRepository.getById(id);
        Vacancy vacancy = rs.getVacancy();
        Long authorId = vacancy.getAuthor().getId();
        return vacancyRepository.getCountVacanciesByAuthorId(authorId);
    }

    @Override
    public String getApplicantNameById(Long id) {
        RespondApplicant rs = respondedApplicantRepository.getById(id);
        Resume resume = rs.getResume();
        User author = resume.getApplicant();
        return author.getName() + " " + author.getSurname();
    }

    @Override
    public Integer getCountOfResumes(Long id) {
        RespondApplicant rs = respondedApplicantRepository.getById(id);
        Resume resume = rs.getResume();
        Long authorId = resume.getApplicant().getId();
        return resumeRepository.countByApplicantId(authorId);
    }

    @Override
    public Long getRecipientId(Long respondedApplicantId, Long authorId) {
        RespondApplicant rs = respondedApplicantRepository.getById(respondedApplicantId);
        Resume resume = rs.getResume();
        Vacancy vacancy = rs.getVacancy();
        if (Objects.equals(authorId, resume.getApplicant().getId())){
            return vacancy.getAuthor().getId();
        } else {
            return resume.getApplicant().getId();
        }
    }

    @Override
    @SneakyThrows
    public List<ResponseEmployerDto> getEmployerResponsesByVacancyId(Authentication authentication, Long vacancyId) {
        if (!vacancyRepository.existsById(vacancyId)){
            log.error("Попытка запросить отклики на вакансию которой не существует, ID : {}", vacancyId);
            throw new VacancyException("Вакансии с данным ID (" + vacancyId + ") не существует");
        }
        User user = (User) authentication.getPrincipal();
        if (!Objects.equals(vacancyRepository.findById(vacancyId).get().getAuthor().getId(), user.getId())){
            log.error("Попытка запросить отклики на вакансию не являясь автором вакансии, ID вакансии: {}, ID хакера {}", vacancyId, user.getId());
            throw new VacancyException("Вы не являетесь автором вакансии и не можете просмотреть отклики на эту вакансию");
        }
        List<RespondApplicant> list = respondedApplicantRepository.getRespondedApplicantsByVacancyId(vacancyId);
        return getEmployerResponseDtos(list);
    }

    private List<ResponseEmployerDto> getEmployerResponseDtos(List<RespondApplicant> list) {
        List<ResponseEmployerDto> employerDtoList = new ArrayList<>();
        for (RespondApplicant cur : list) {
            User author = cur.getResume().getApplicant();
            employerDtoList.add(ResponseEmployerDto.builder()
                    .vacancyName(cur.getVacancy().getName())
                    .vacancyId(cur.getVacancy().getId())
                    .resumeId(cur.getResume().getId())
                    .resumeName(cur.getResume().getName())
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
                    .resumedId(respondApplicant.getResume().getId())
                    .resumeName(respondApplicant.getResume().getName())
                    .vacancyName(respondApplicant.getVacancy().getName())
                    .vacancyId(respondApplicant.getVacancy().getId())
                    .status(getApplicantStatusByBoolean(respondApplicant.getConfirmation()))
                    .build());
        }
        return responses;
    }

    private String getApplicantStatusByBoolean(Boolean confirmation) {
        if (confirmation == null) {
            return ResponseStatus.IN_PROGRESS.getValue();
        } else if (confirmation) {
            return ResponseStatus.APPROVED.getValue();
        } else {
            return ResponseStatus.REJECTED.getValue();
        }
    }

    private String getEmployerStatusByBoolean(Boolean confirmation) {
        if (confirmation == null) {
            return ResponseStatus.IN_PROGRESS.getValue();
        } else if (confirmation) {
            return ResponseStatus.APPROVED.getValue();
        } else {
            return ResponseStatus.REJECTED.getValue();
        }
    }
}
