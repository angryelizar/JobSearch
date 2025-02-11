package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.AvatarImageDto;
import org.example.jobsearch.models.User;
import org.example.jobsearch.repositories.UserRepository;
import org.example.jobsearch.service.AvatarImageService;
import org.example.jobsearch.util.FileUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvatarImageServiceImpl implements AvatarImageService {
    private final UserRepository userRepository;
    private final FileUtil fileUtil;

    public void upload(Authentication auth, AvatarImageDto avatarImageDto) {
        User user = userRepository.getByEmail(auth.getName()).get();
        String fileName = fileUtil.saveUploadedFile(avatarImageDto.getFile(), "images");
        userRepository.setAvatar(user.getId(), fileName);
    }

    public void upload(User user, MultipartFile file) {
        String fileName = fileUtil.saveUploadedFile(file, "images");
        userRepository.setAvatar(user.getId(), fileName);
    }

    @Override
    public ResponseEntity<?> download(Long id) {
        String fileName = userRepository.getAvatarByUserId(id);
        return fileUtil.getOutputFile(fileName, "/images", MediaType.IMAGE_JPEG);
    }
}
