package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dao.AuthorityDao;
import org.example.jobsearch.service.AuthorityService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityDao authorityDao;
    @Override
    public void add(Long userId, Long authorityId) {
        authorityDao.add(userId, authorityId);
    }

    @Override
    public Long getAccountAuthorityByTypeString(String type) {
        if (type.equalsIgnoreCase("Работодатель")){
            return authorityDao.getAuthorityIdByAccountType("EMPLOYER");
        }
        return authorityDao.getAuthorityIdByAccountType("APPLICANT");
    }

}
