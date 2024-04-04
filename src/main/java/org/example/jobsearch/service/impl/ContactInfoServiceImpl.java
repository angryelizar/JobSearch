package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dao.ContactInfoDao;
import org.example.jobsearch.dto.ContactInfoDto;
import org.example.jobsearch.dto.PageContactInfoDto;
import org.example.jobsearch.models.ContactInfo;
import org.example.jobsearch.service.ContactInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoDao contactInfoDao;
    @Override
    public List<ContactInfoDto> getContactInfosByResumeId(Long id) {
        List<ContactInfoDto> contactInfoDtos = new ArrayList<>();
        List<ContactInfo> infoList = contactInfoDao.getContactInfosByResumeId(id);
        infoList.forEach(e -> contactInfoDtos.add(
                ContactInfoDto.builder()
                        .typeId(e.getTypeId())
                        .type(contactInfoDao.getContactInfoType(e.getTypeId()))
                        .content(e.getContent())
                        .build()
        ));
        return contactInfoDtos;
    }

    public void addContactInfo(ContactInfoDto contactInfoDto, Long resumeId){
        contactInfoDao.addContactInfo(
                ContactInfo.builder()
                        .resumeId(resumeId)
                        .content(contactInfoDto.getContent())
                        .typeId(contactInfoDto.getTypeId())
                        .build()
        );
    }

    @Override
    public List<PageContactInfoDto> getPageContactInfoByResumeId(Long id) {
        List<ContactInfo> contactInfos = contactInfoDao.getContactInfosByResumeId(id);
        List<PageContactInfoDto> contactInfoDtos = new ArrayList<>();
        for (int i = 0; i < contactInfos.size(); i++) {
            ContactInfo curContact = contactInfos.get(i);
            contactInfoDtos.add(
                    PageContactInfoDto
                            .builder()
                            .type(contactInfoDao.getContactInfoType(curContact.getTypeId()))
                            .content(curContact.getContent())
                            .build()
            );
        }
        return contactInfoDtos;
    }
}
