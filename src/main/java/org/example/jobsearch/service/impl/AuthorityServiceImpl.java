package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.Role;
import org.example.jobsearch.repositories.AuthorityRepository;
import org.example.jobsearch.repositories.RoleRepository;
import org.example.jobsearch.repositories.UserRepository;
import org.example.jobsearch.service.AuthorityService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void add(Long userId, Long authorityId) {
        roleRepository.save(Role.builder()
                .user(userRepository.findById(userId).get())
                .authority(authorityRepository.findById(authorityId).get())
                .build());
    }

    @Override
    public Long getAccountAuthorityByTypeString(String type) {
        if (type.equalsIgnoreCase("Работодатель")) {
            return authorityRepository.getAuthorityIdByAccountType("EMPLOYER");
        }
        return authorityRepository.getAuthorityIdByAccountType("APPLICANT");
    }

}
