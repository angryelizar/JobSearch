package org.example.jobsearch.service;

import org.example.jobsearch.dto.AvatarImageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AvatarImageService {
    void upload(Authentication authentication, AvatarImageDto avatarImageDto);

    ResponseEntity<?> download(Long id);
}
