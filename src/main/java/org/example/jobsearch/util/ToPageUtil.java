package org.example.jobsearch.util;

import org.example.jobsearch.dto.ProfilePageResumeDto;
import org.example.jobsearch.dto.ProfilePageVacancyDto;
import org.example.jobsearch.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToPageUtil {
    public static Page<ProfilePageVacancyDto> toPageVacancy(List<ProfilePageVacancyDto> vacancies, Pageable pageable) {
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize() > vacancies.size() ? vacancies.size() : pageable.getOffset() + pageable.getPageSize()));
        List<ProfilePageVacancyDto> subList = vacancies.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, vacancies.size());
    }

    public static Page<ProfilePageResumeDto> toPageResume(List<ProfilePageResumeDto> resumes, Pageable pageable) {
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize() > resumes.size() ? resumes.size() : pageable.getOffset() + pageable.getPageSize()));
        List<ProfilePageResumeDto> subList = resumes.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, resumes.size());
    }

    public static Page<UserDto> toPageEmployers(List<UserDto> employers, Pageable pageable) {
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize() > employers.size() ? employers.size() : pageable.getOffset() + pageable.getPageSize()));
        List<UserDto> subList = employers.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, employers.size());
    }
}
