package org.example.jobsearch.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthorityService {
    void add(Long userId, Long authorityId);
    Long getAccountAuthorityByTypeString(String type);
}
