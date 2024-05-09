package org.example.jobsearch.service;

import org.example.jobsearch.models.Authority;
import org.springframework.stereotype.Service;

@Service
public interface AuthorityService {
    Authority getAccountAuthorityByTypeString(String type);
}
