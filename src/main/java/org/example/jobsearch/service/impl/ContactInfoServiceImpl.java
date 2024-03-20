package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dao.ContactInfoDao;
import org.example.jobsearch.dto.ContactInfoDto;
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
}
