package org.example.jobsearch.service;

import org.example.jobsearch.dto.AvatarImageDto;
import org.example.jobsearch.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AvatarImageService {
    void upload(Authentication authentication, AvatarImageDto avatarImageDto);
    void upload(User user, MultipartFile file);

    ResponseEntity<?> download(Long id);
}
