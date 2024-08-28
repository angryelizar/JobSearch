package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.ContactDto;
import org.example.jobsearch.dto.MessageDto;
import org.example.jobsearch.dto.SendMessageDto;
import org.example.jobsearch.exceptions.EmptyMessageException;
import org.example.jobsearch.exceptions.UserException;
import org.example.jobsearch.models.Message;
import org.example.jobsearch.models.RespondApplicant;
import org.example.jobsearch.models.User;
import org.example.jobsearch.repositories.MessageRepository;
import org.example.jobsearch.repositories.RespondedApplicantRepository;
import org.example.jobsearch.repositories.UserRepository;
import org.example.jobsearch.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RespondedApplicantRepository respondedApplicantRepository;
    private final MessageRepository messageRepository;

    @Override
    public List<ContactDto> messagesGet(Authentication auth) {
        List<ContactDto> result = new ArrayList<>();
        if (userService.isApplicant(auth.getName())) {
            List<RespondApplicant> list = respondedApplicantRepository.findAllByApplicantEmail(auth.getName());
            for (RespondApplicant cur : list) {
                result.add(ContactDto.builder()
                        .respondedApplicantId(cur.getId())
                        .vacancyName(cur.getVacancy().getName())
                        .name(cur.getVacancy().getAuthor().getName() + " " + cur.getVacancy().getAuthor().getSurname())
                        .build());
            }
        } else {
            List<RespondApplicant> list = respondedApplicantRepository.getByEmployerEmail(auth.getName());
            for (RespondApplicant cur : list) {
                Long authorId = cur.getResume().getApplicant().getId();
                User user = userRepository.findById(authorId).get();
                String name = user.getName() + " " + user.getSurname();
                result.add(ContactDto.builder()
                        .respondedApplicantId(cur.getId())
                        .vacancyName(cur.getVacancy().getName())
                        .name(name)
                        .build());
            }
        }
        return result;
    }

    @Override
    public List<MessageDto> messageGetByRespondedApplicantId(Long id) {
        List<Message> messageList = messageRepository.messageGetByRespondedApplicantId(id);
        List<MessageDto> result = new ArrayList<>();
        for (Message cur : messageList) {
            result.add(MessageDto.builder()
                    .author(cur.getFromTo().getName() + " " + cur.getFromTo().getSurname())
                    .dateTime(cur.getDateTime())
                    .content(cur.getContent())
                    .build());
        }
        return result;
    }
    
    private MessageDto makeMessageDto(Message message) {
        return MessageDto.builder()
                .author(message.getFromTo().getName() + " " + message.getFromTo().getSurname())
                .dateTime(message.getDateTime())
                .content(message.getContent())
                .build();
    }

    @Override
    @SneakyThrows
    public void sendMessage(SendMessageDto messageDto, Authentication auth) {
        User user = userRepository.getByEmail(auth.getName()).get();
        if (!Objects.equals(user.getId(), messageDto.getMessageAuthor())){
            throw new UserException("exception.message.not.user");
        }
        Message message = validateAndBuildMessage(messageDto);
        messageRepository.save(message);
    }

    @Override
    public MessageDto sendMessage(SendMessageDto messageDto, Long id) {
        Optional<RespondApplicant> respondApplicant =  respondedApplicantRepository.findById(id);
        if (respondApplicant.isEmpty()) {
            throw new EmptyMessageException("exception.response.notFound");
        }
        Message message = validateAndBuildMessage(messageDto);
        return makeMessageDto(messageRepository.save(message));
    }

    private Message validateAndBuildMessage(SendMessageDto messageDto) {
        if (messageDto.getMessageText().isBlank() || messageDto.getMessageText().isEmpty()){
            throw new EmptyMessageException("exception.message.empty");
        }
        return Message.builder()
                .content(messageDto.getMessageText())
                .fromTo(userRepository.findById(messageDto.getMessageAuthor()).get())
                .toFrom(userRepository.findById(messageDto.getMessageRecipient()).get())
                .dateTime(LocalDateTime.now())
                .respondedApplicants(respondedApplicantRepository.findById(messageDto.getRespondApplicant()).get())
                .build();
    }
}
