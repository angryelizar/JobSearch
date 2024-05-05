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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
    public ProfilePageDto profileGet(Authentication authentication, Pageable pageable) {
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
            resumes.sort(Comparator.comparing(Resume::getUpdateTime).reversed());
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
            profilePageDTO.setResumes(toPageResume(pageResumeDtos, pageable));
        } else if (user.getAccountType().equalsIgnoreCase("Работодатель")) {
            List<Vacancy> vacancies = vacancyService.getVacanciesByEmployerId(user.getId());
            List<ProfilePageVacancyDto> pageVacancyDtos = new ArrayList<>();
            vacancies.sort(Comparator.comparing(Vacancy::getUpdateTime).reversed());
            for (Vacancy vacancy : vacancies) {
                pageVacancyDtos.add(
                        ProfilePageVacancyDto.builder()
                                .id(vacancy.getId())
                                .name(vacancy.getName())
                                .salary(vacancy.getSalary())
                                .updateDate(DateUtil.getFormattedLocalDateTime(vacancy.getUpdateTime()))
                                .build()
                );
            }
            profilePageDTO.setVacancies(toPageVacancy(pageVacancyDtos, pageable));
        }
        return profilePageDTO;
    }

    private Page<ProfilePageVacancyDto> toPageVacancy(List<ProfilePageVacancyDto> vacancies, Pageable pageable) {
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize() > vacancies.size() ? vacancies.size() : pageable.getOffset() + pageable.getPageSize()));
        List<ProfilePageVacancyDto> subList = vacancies.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, vacancies.size());
    }

    private Page<ProfilePageResumeDto> toPageResume(List<ProfilePageResumeDto> resumes, Pageable pageable) {
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize() > resumes.size() ? resumes.size() : pageable.getOffset() + pageable.getPageSize()));
        List<ProfilePageResumeDto> subList = resumes.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, resumes.size());
    }


}
