package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.RespondedApplicantDao;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dao.VacancyDao;
import org.example.jobsearch.dto.ContactDto;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserService userService;
    private final RespondedApplicantDao respondedApplicantDao;
    private final VacancyService vacancyService;
    private final UserDao userDao;
    private final VacancyDao vacancyDao;
    private final ResumeService resumeService;

    @Override
    public List<ContactDto> messagesGet(Authentication auth) {
        List<ContactDto> result = new ArrayList<>();
        if (userService.isApplicant(auth.getName())) {
            List<RespondApplicant> list = respondedApplicantDao.getByApplicantEmail(auth.getName());
            for (RespondApplicant cur : list) {
                result.add(ContactDto.builder()
                        .respondedApplicantId(cur.getId())
                        .vacancyName(vacancyService.getNameById(cur.getVacancyId()))
                        .name(userDao.getUserNameById(vacancyDao.getVacancyById(cur.getVacancyId()).get().getAuthorId()))
                        .build());
            }
        } else {
            List<RespondApplicant> list = respondedApplicantDao.getByEmployerEmail(auth.getName());
            for (RespondApplicant cur : list) {
                Long authorId = resumeService.getAuthorByResumeId(cur.getResumeId()).getId();
                String name = userDao.getUserNameById(authorId) + " " + userDao.getSurnameNameById(authorId);
                result.add(ContactDto.builder()
                        .respondedApplicantId(cur.getId())
                        .vacancyName(vacancyService.getNameById(cur.getVacancyId()))
                        .name(name)
                        .build());
            }
        }
        return result;
    }
}
