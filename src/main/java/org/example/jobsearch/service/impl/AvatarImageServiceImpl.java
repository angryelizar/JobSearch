package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dto.AvatarImageDto;
import org.example.jobsearch.service.AvatarImageService;
import org.example.jobsearch.util.FileUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvatarImageServiceImpl implements AvatarImageService {
    private final UserDao userDao;
    private final FileUtil fileUtil;

    public void upload(Long id, AvatarImageDto avatarImageDto) {
        String fileName = fileUtil.saveUploadedFile(avatarImageDto.getFile(), "images");
        userDao.setAvatar(id, fileName);
    }

    @Override
    public ResponseEntity<?> download(Long id) {
        String fileName = userDao.getAvatarByUserId(id);
        return fileUtil.getOutputFile(fileName, "/images", MediaType.IMAGE_JPEG);
    }
}
