package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.example.jobsearch.service.ProfileService;
import org.example.jobsearch.service.ResumeService;
import org.example.jobsearch.service.UserService;
import org.example.jobsearch.service.VacancyService;
import org.example.jobsearch.util.DateUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserService userService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;

    @Override
    public ProfileDto getProfile(Authentication auth) throws UserNotFoundException {
        User user = userService.getFullUserByEmail(auth.getName());
        return ProfileDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .accountType(user.getAccountType())
                .build();
    }

    @Override
    @SneakyThrows
    public ProfilePageDto profileGet(Authentication authentication) {
        ProfilePageDto profilePageDTO = new ProfilePageDto();
        User user = userService.getFullUserByEmail(authentication.getName());
        profilePageDTO.setUser(
                UserDto.builder()
                        .name(user.getName())
                        .surname(user.getSurname())
                        .avatar(user.getAvatar())
                        .age(user.getAge())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .accountType(user.getAccountType())
                        .build()
        );
        if (user.getAccountType().equalsIgnoreCase("Соискатель")) {
            List<Resume> resumes = resumeService.getFullResumesByUserId(user.getId());
            List<ProfilePageResumeDto> pageResumeDtos = new ArrayList<>();
            for (Resume resume : resumes) {
                pageResumeDtos.add(
                        ProfilePageResumeDto.builder()
                                .id(resume.getId())
                                .name(resume.getName())
                                .salary(resume.getSalary())
                                .updateDate(DateUtil.getFormattedLocalDateTime(resume.getUpdateTime()))
                                .build()
                );
            }
            profilePageDTO.setResumes(pageResumeDtos);
        } else if (user.getAccountType().equalsIgnoreCase("Работодатель")) {
            List<Vacancy> vacancies = vacancyService.getVacanciesByEmployerId(user.getId());
            List<ProfilePageVacancyDto> pageVacancyDtos = new ArrayList<>();
            for (Vacancy vacancy : vacancies) {
                pageVacancyDtos.add(
                        ProfilePageVacancyDto.builder()
                                .id(vacancy.getId())
                                .name(vacancy.getName())
                                .salary(vacancy.getSalary())
                                .updateDate(DateUtil.getFormattedLocalDateTime(vacancy.getUpdateTime()))
                                .build()
                );
                profilePageDTO.setVacancies(pageVacancyDtos);
            }
        }
        return profilePageDTO;
    }


}
