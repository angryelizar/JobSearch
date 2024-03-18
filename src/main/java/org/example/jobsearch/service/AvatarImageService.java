package org.example.jobsearch.service;

import org.example.jobsearch.dto.AvatarImageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AvatarImageService {
    void upload(Long id, AvatarImageDto avatarImageDto);

    ResponseEntity<?> download(Long id);
}
