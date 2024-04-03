package org.example.jobsearch.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.ProfileDto;
import org.example.jobsearch.dto.ResumeDto;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.service.ProfileService;
import org.example.jobsearch.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/profile")
public class ProfileController {
    private final ProfileService profileService;
    @GetMapping()
    public ResponseEntity<ProfileDto> getProfile(Authentication auth) throws UserNotFoundException {
        return ResponseEntity.ok(profileService.getProfile(auth));
    }
}
