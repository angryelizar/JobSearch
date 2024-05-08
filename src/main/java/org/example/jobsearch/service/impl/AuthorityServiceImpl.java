package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.models.Authority;
import org.example.jobsearch.repositories.AuthorityRepository;
import org.example.jobsearch.service.AuthorityService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Override
    public Authority getAccountAuthorityByTypeString(String type) {
        if (type.equalsIgnoreCase("Работодатель")) {
            return authorityRepository.getAuthorityByAccountType("EMPLOYER");
        }
        return authorityRepository.getAuthorityByAccountType("APPLICANT");
    }

}
