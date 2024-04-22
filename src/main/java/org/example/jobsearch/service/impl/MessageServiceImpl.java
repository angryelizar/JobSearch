package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.MessageDao;
import org.example.jobsearch.dao.RespondedApplicantDao;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dao.VacancyDao;
import org.example.jobsearch.dto.ContactDto;
import org.example.jobsearch.dto.MessageDto;
import org.example.jobsearch.dto.SendMessageDto;
import org.example.jobsearch.exceptions.EmptyMessageException;
import org.example.jobsearch.exceptions.UserException;
import org.example.jobsearch.models.Message;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.User;
import org.example.jobsearch.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final MessageDao messageDao;

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

    @Override
    public List<MessageDto> messageGetByRespondedApplicantId(Long id) {
        List<Message> messageList = messageDao.messageGetByRespondedApplicantId(id);
        List<MessageDto> result = new ArrayList<>();
        for (Message cur : messageList) {
            result.add(MessageDto.builder()
                    .author(userDao.getUserNameById(cur.getFromTo()))
                    .dateTime(cur.getDateTime())
                    .content(cur.getContent())
                    .build());
        }
        return result;
    }

    @Override
    @SneakyThrows
    public void sendMessage(SendMessageDto messageDto, Authentication auth) {
        User user = userDao.getUserByEmail(auth.getName()).get();
        if (!Objects.equals(user.getId(), messageDto.getMessageAuthor())){
            throw new UserException("Вы пытаетесь выдать себя за другого пользователя!");
        }
        if (messageDto.getMessageText().isBlank() || messageDto.getMessageText().isEmpty()){
            throw new EmptyMessageException("Нельзя отправлять пустой текст");
        }
        Message message = Message.builder()
                .content(messageDto.getMessageText())
                .fromTo(messageDto.getMessageAuthor())
                .toFrom(messageDto.getMessageRecipient())
                .dateTime(LocalDateTime.now())
                .respondApplicantId(messageDto.getRespondApplicant())
                .build();
        messageDao.create(message);
    }
}
