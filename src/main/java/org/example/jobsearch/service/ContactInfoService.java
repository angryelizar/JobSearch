package org.example.jobsearch.service;

import org.example.jobsearch.dto.ContactInfoDto;
import org.example.jobsearch.models.ContactInfo;

import java.util.List;

public interface ContactInfoService {

    List<ContactInfoDto> getContactInfosByResumeId(Long id);
    void addContactInfo(ContactInfoDto contactInfoDto, Long resumeId);
}
