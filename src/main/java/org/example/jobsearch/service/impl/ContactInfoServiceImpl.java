package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.ContactInfoDto;
import org.example.jobsearch.dto.PageContactInfoDto;
import org.example.jobsearch.models.ContactInfo;
import org.example.jobsearch.repositories.ContactInfoRepository;
import org.example.jobsearch.repositories.ContactTypeRepository;
import org.example.jobsearch.repositories.ResumeRepository;
import org.example.jobsearch.service.ContactInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public List<ContactInfoDto> getContactInfosByResumeId(Long id) {
        List<ContactInfoDto> contactInfoDtos = new ArrayList<>();
        List<ContactInfo> infoList = contactInfoRepository.getContactInfosByResumeId(id);
        infoList.forEach(e -> contactInfoDtos.add(
                ContactInfoDto.builder()
                        .typeId(e.getContactType().getId())
                        .type(e.getContactType().getType())
                        .content(e.getContent())
                        .build()
        ));
        return contactInfoDtos;
    }

    public void addContactInfo(ContactInfoDto contactInfoDto, Long resumeId) {
        contactInfoRepository.save(ContactInfo.builder()
                .resume(resumeRepository.findById(resumeId).get())
                .content(contactInfoDto.getContent())
                .contactType(contactTypeRepository.findById(contactInfoDto.getTypeId()).get())
                .build());
    }

    @Override
    public List<PageContactInfoDto> getPageContactInfoByResumeId(Long id) {
        List<ContactInfo> contactInfos = contactInfoRepository.getContactInfosByResumeId(id);
        List<PageContactInfoDto> contactInfoDtos = new ArrayList<>();
        for (ContactInfo curContact : contactInfos) {
            contactInfoDtos.add(
                    PageContactInfoDto
                            .builder()
                            .type(curContact.getContactType().getType())
                            .content(curContact.getContent())
                            .build()
            );
        }
        return contactInfoDtos;
    }

    @Override
    public Long getContactInfoIdByType(String type) {
        return contactInfoRepository.findContactTypeIdByQuery(type);
    }
}
