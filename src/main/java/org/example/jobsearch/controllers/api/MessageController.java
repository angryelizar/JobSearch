package org.example.jobsearch.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.ContactDto;
import org.example.jobsearch.dto.MessageDto;
import org.example.jobsearch.dto.SendMessageDto;
import org.example.jobsearch.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("restMessages")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/messages")
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<ContactDto>> messagesGet(Authentication authentication) {
        return ResponseEntity.ok(messageService.messagesGet(authentication));
    }

    @GetMapping("/response/{id}")
    public ResponseEntity<List<MessageDto>> messageGetByRespondedApplicantId(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.messageGetByRespondedApplicantId(id));
    }

    @PostMapping("/response")
    public ResponseEntity<HttpStatus> sendMessage(@RequestBody SendMessageDto messageDto, Authentication auth) {
        messageService.sendMessage(messageDto, auth);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
