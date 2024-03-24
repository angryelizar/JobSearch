package org.example.jobsearch.service;

import org.example.jobsearch.dto.ProfileDto;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {

    ProfileDto getProfile(Authentication auth) throws UserNotFoundException;
}
