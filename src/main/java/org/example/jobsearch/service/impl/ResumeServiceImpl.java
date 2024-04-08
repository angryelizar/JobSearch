package org.example.jobsearch.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.*;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.ResumeNotFoundException;
import org.example.jobsearch.exceptions.VacancyException;
import org.example.jobsearch.models.EducationInfo;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.WorkExperienceInfo;
import org.example.jobsearch.service.ContactInfoService;
import org.example.jobsearch.service.EducationInfoService;
import org.example.jobsearch.service.ResumeService;
import org.example.jobsearch.service.WorkExperienceInfoService;
import org.example.jobsearch.util.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {
    private final ResumeDao resumeDao;
    private final UserDao userDao;
    private final WorkExperienceInfoDao workExperienceInfoDao;
    private final EducationInfoDao educationInfoDao;
    private final CategoryDao categoryDao;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final EducationInfoService educationInfoService;
    private final ContactInfoService contactInfoService;

    @Override
    public List<ResumeDto> getResumes() {
        List<Resume> resumes = resumeDao.getResumes();
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getActiveResumes() {
        List<Resume> resumes = resumeDao.getActiveResumes();
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getInActiveResumes() {
        List<Resume> resumes = resumeDao.getInActiveResumes();
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long id) throws ResumeNotFoundException {
        List<Resume> resumes = resumeDao.getResumesByCategoryId(id);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Резюме в категории нет или категории не существует");
        }
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByUserId(Long id) throws ResumeNotFoundException {
        List<Resume> resumes = resumeDao.getResumesByUserId(id);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Пользователь с этим ID либо не публиковал резюме - либо его нет :(");
        }
        return getResumeDtos(resumes);
    }

    @Override
    public List<Resume> getFullResumesByUserId(Long id) {
        return resumeDao.getResumesByUserId(id);
    }

    @Override
    public List<ResumeDto> getResumesByName(String query) {
        List<Resume> resumes = resumeDao.getResumesByName(query);
        return getResumeDtos(resumes);
    }

    @Override
    public List<PageResumeDto> getActivePageResumes() {
        List<PageResumeDto> pageResumeDtos = new ArrayList<>();
        List<Resume> resumes = resumeDao.getActiveResumes();
        resumes.sort(Comparator.comparing(Resume::getUpdateTime).reversed());
        for (Resume curRes : resumes) {
            pageResumeDtos.add(PageResumeDto
                    .builder()
                    .id(curRes.getId())
                    .category(categoryDao.getCategoryNameById(curRes.getCategoryId()))
                    .name(curRes.getName())
                    .author(userDao.getUserNameById(curRes.getApplicantId()) + " " + userDao.getSurnameNameById(curRes.getApplicantId()))
                    .updatedDate(DateUtil.getFormattedLocalDateTime(curRes.getUpdateTime()))
                    .salary(curRes.getSalary())
                    .build());
        }
        return pageResumeDtos;
    }

    @Override
    public Page<PageResumeDto> getActivePageResumes(Integer page) {
        List<PageResumeDto> resumes = getActivePageResumes();
        if (page < 0){
            page = 0;
        }
        return toPage(resumes, PageRequest.of(page, 5));
    }

    private Page<PageResumeDto> toPage(List<PageResumeDto> resumes, Pageable pageable) {
        if (pageable.getOffset() >= resumes.size()){
            log.error("Я пока не понял как эту ситуацию обработать....");
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize() > resumes.size() ? resumes.size() : pageable.getOffset() + pageable.getPageSize()));
        List<PageResumeDto> subList = resumes.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, resumes.size());
    }


    @Override
    public List<ProfileAndResumesDto> getResumesByApplicantName(String user) throws ResumeNotFoundException {
        List<User> users = new ArrayList<>(userDao.getUsersByName(user));
        List<ProfileAndResumesDto> resAndUsers = new ArrayList<>();
        for (User currUsr : users) {
            resAndUsers.add(
                    ProfileAndResumesDto.builder()
                            .name(currUsr.getName())
                            .surname(currUsr.getSurname())
                            .age(currUsr.getAge())
                            .email(currUsr.getEmail())
                            .resumeDtos(getResumesByUserId((currUsr.getId())))
                            .build()
            );
        }
        return resAndUsers;
    }

    @Override
    @SneakyThrows
    public void createResume(Authentication authentication, ResumeDto resumeDto) {
        if (userDao.userIsEmployer(resumeDto.getApplicantId()) || !userDao.idIsExists(resumeDto.getApplicantId())) {
            throw new ResumeException("Пользователь либо работодатель, либо его не существует!");
        }
        if (Boolean.FALSE.equals(categoryDao.isExists(resumeDto.getCategoryId()))) {
            throw new ResumeException("Выбранной вами категории не существует");
        }
        if (resumeDto.getSalary() <= 0) {
            throw new ResumeException("Зарплата не может быть меньше или равна нулю");
        }
        Long applicantId = userDao.getUserByEmail(authentication.getName()).get().getId();
        Long resumeId = resumeDao.createResume(Resume.builder()
                .applicantId(applicantId)
                .name(resumeDto.getName())
                .categoryId(resumeDto.getCategoryId())
                .salary(resumeDto.getSalary())
                .isActive(resumeDto.getIsActive())
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());
        if (!resumeDto.getEducationInfos().isEmpty()) {
            resumeDto.getEducationInfos().forEach(e -> educationInfoDao.createEducationInfo(
                    EducationInfo.builder()
                            .resumeId(resumeId)
                            .institution(e.getInstitution())
                            .program(e.getProgram())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .degree(e.getDegree())
                            .build()
            ));
        }
        if (!resumeDto.getWorkExperienceInfos().isEmpty()) {
            resumeDto.getWorkExperienceInfos().forEach(e -> workExperienceInfoDao.createWorkExperienceInfo(
                    WorkExperienceInfo.builder()
                            .resumeId(resumeId)
                            .years(e.getYears())
                            .companyName(e.getCompanyName())
                            .position(e.getPosition())
                            .responsibilities(e.getResponsibilities())
                            .build()
            ));
        }
        for (int i = 0; i < resumeDto.getContactInfos().size(); i++) {
            contactInfoService.addContactInfo(
                    resumeDto.getContactInfos().get(i), resumeId
            );
        }
    }

    @Override
    @SneakyThrows
    public void editResume(Long id, UpdateResumeDto updateResumeDto) {
        if (Boolean.FALSE.equals(categoryDao.isExists(updateResumeDto.getCategoryId()))) {
            throw new ResumeException("Выбранной вами категории не существует");
        }
        if (updateResumeDto.getSalary() <= 0) {
            throw new ResumeException("Зарплата не может быть меньше или равна нулю");
        }
        resumeDao.editResume(Resume.builder()
                .name(updateResumeDto.getName())
                .categoryId(updateResumeDto.getCategoryId())
                .salary(updateResumeDto.getSalary())
                .isActive(updateResumeDto.getIsActive())
                .updateTime(LocalDateTime.now())
                .build(), id);
        if (!updateResumeDto.getEducationInfos().isEmpty()) {
            updateResumeDto.getEducationInfos().forEach(e -> educationInfoDao.editEducationInfo(
                    EducationInfo.builder()
                            .institution(e.getInstitution())
                            .program(e.getProgram())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .degree(e.getDegree())
                            .build(), id
            ));
        }
        if (!updateResumeDto.getWorkExperienceInfos().isEmpty()) {
            updateResumeDto.getWorkExperienceInfos().forEach(e -> workExperienceInfoDao.editWorkExperienceInfo(
                    WorkExperienceInfo.builder()
                            .years(e.getYears())
                            .companyName(e.getCompanyName())
                            .position(e.getPosition())
                            .responsibilities(e.getResponsibilities())
                            .build(), id
            ));
        }
    }

    @Override
    public void deleteResumeById(Long id) {
        resumeDao.deleteResumeById(id);
    }

    @Override
    @SneakyThrows
    public void deleteResumeById(Long id, Authentication auth) {
        if (!resumeDao.idIsExists(id)) {
            log.info("Была попытка удалить несуществующую вакансию с ID " + id);
            throw new ResumeException("Такой вакансии нет! ID " + id);
        }
        Resume resume = resumeDao.getResumeById(id).get();
        if (!Objects.equals(resume.getApplicantId(), userDao.getUserByEmail(auth.getName()).get().getId())){
            log.info("Была попытка удалить чужую вакансию с ID " + id);
            throw new ResumeException("Это не ваше резюме - вы не можете его удалить!");
        }
        resumeDao.deleteResumeById(id);
    }

    @Override
    @SneakyThrows
    public void update(Long id) {
        if (resumeDao.idIsExists(id)) {
            resumeDao.setUpdateTime(LocalDateTime.now(), id);
        } else {
            log.error("Было запрошено несуществующее резюме с ID " + id);
            throw new ResumeException("Этого резюме не существует");
        }
    }

    @Override
    @SneakyThrows
    public PageResumeDto getPageResumeById(Long id) {
        if (resumeDao.idIsExists(id)) {
            Resume resume = resumeDao.getResumeById(id).get();
            return PageResumeDto.builder()
                    .name(resume.getName())
                    .category(categoryDao.getCategoryNameById(resume.getCategoryId()))
                    .author(userDao.getUserNameById(resume.getApplicantId()) + " " + userDao.getSurnameNameById(resume.getApplicantId()))
                    .id(resume.getId())
                    .salary(resume.getSalary())
                    .updatedDate(DateUtil.getFormattedLocalDate(resume.getUpdateTime()))
                    .build();
        } else {
            log.error("Было запрошено несуществующее резюме с ID " + id);
            throw new ResumeException("Этого резюме не существует");
        }
    }

    @Override
    public void addResumeFromForm(CreatePageResumeDto pageResumeDto, HttpServletRequest request, Authentication auth, String telegram, String whatsapp, String telephone, String linkedin, String email) {
        String isActive = request.getParameter("isActive");
        pageResumeDto.setIsActive("on".equals(isActive));
        Resume resume = Resume.builder()
                .applicantId(userDao.getUserByEmail(auth.getName()).get().getId())
                .name(pageResumeDto.getName())
                .categoryId(pageResumeDto.getCategoryId())
                .salary(pageResumeDto.getSalary())
                .isActive(pageResumeDto.getIsActive())
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        Long resumeId = resumeDao.createResume(resume);

        WorkExperienceInfo workExperienceInfo = WorkExperienceInfo.builder()
                .resumeId(resumeId)
                .years(pageResumeDto.getWorkExperienceInfo().getYears())
                .companyName(pageResumeDto.getWorkExperienceInfo().getCompanyName())
                .position(pageResumeDto.getWorkExperienceInfo().getPosition())
                .responsibilities(pageResumeDto.getWorkExperienceInfo().getResponsibilities())
                .build();
        workExperienceInfoDao.createWorkExperienceInfo(workExperienceInfo);

        EducationInfo educationInfo = EducationInfo.builder()
                .resumeId(resumeId)
                .institution(pageResumeDto.getEducationInfo().getInstitution())
                .program(pageResumeDto.getEducationInfo().getProgram())
                .degree(pageResumeDto.getEducationInfo().getDegree())
                .startDate(pageResumeDto.getEducationInfo().getStartDate())
                .endDate(pageResumeDto.getEducationInfo().getEndDate())
                .build();
        educationInfoDao.createEducationInfo(educationInfo);

        if (!whatsapp.isEmpty() && !whatsapp.isBlank()) {
            contactInfoService.addContactInfo(
                    ContactInfoDto.builder()
                            .typeId(contactInfoService.getContactInfoIdByType("WhatsApp"))
                            .content(whatsapp)
                            .build(), resumeId
            );
        }
        if (!telegram.isEmpty() && !telegram.isBlank()) {
            contactInfoService.addContactInfo(
                    ContactInfoDto.builder()
                            .typeId(contactInfoService.getContactInfoIdByType("Telegram"))
                            .content(telegram)
                            .build(), resumeId
            );
        }
        if (!telephone.isEmpty() && !telephone.isBlank()) {
            contactInfoService.addContactInfo(
                    ContactInfoDto.builder()
                            .typeId(contactInfoService.getContactInfoIdByType("Телефон"))
                            .content(telephone)
                            .build(), resumeId
            );
        }
        if (!linkedin.isEmpty() && !linkedin.isBlank()) {
            contactInfoService.addContactInfo(
                    ContactInfoDto.builder()
                            .typeId(contactInfoService.getContactInfoIdByType("Linkedin"))
                            .content(linkedin)
                            .build(), resumeId
            );
        }
        if (!email.isEmpty() && !email.isBlank()) {
            contactInfoService.addContactInfo(
                    ContactInfoDto.builder()
                            .typeId(contactInfoService.getContactInfoIdByType("E-mail"))
                            .content(email)
                            .build(), resumeId
            );
        }
    }

    @Override
    @SneakyThrows
    public List<PageResumeDto> getPageResumeByCategoryId(Long categoryId) {
        if (Boolean.FALSE.equals(categoryDao.isExists(categoryId))) {
            throw new VacancyException("Такой категории нет!");
        }
        List<Resume> resumes = resumeDao.getActiveResumes();
        resumes.sort(Comparator.comparing(Resume::getUpdateTime).reversed());
        List<PageResumeDto> resumeDtos = new ArrayList<>();
        for (Resume curRes : resumes) {
            if (Objects.equals(curRes.getCategoryId(), categoryId)) {
                resumeDtos.add(PageResumeDto.builder()
                        .id(curRes.getId())
                        .category(categoryDao.getCategoryNameById(curRes.getCategoryId()))
                        .salary(curRes.getSalary())
                        .name(curRes.getName())
                        .author(userDao.getUserNameById(curRes.getApplicantId()) + " " + userDao.getSurnameNameById((curRes.getApplicantId())))
                        .updatedDate(DateUtil.getFormattedLocalDateTime(curRes.getUpdateTime()))
                        .build());
            }
        }
        return resumeDtos;
    }

    @Override
    public Page<PageResumeDto> getPageResumeByCategoryId(Long id, Integer page) {
        List<PageResumeDto> result = getPageResumeByCategoryId(id);
        if (page < 0){
            page = 0;
        }
        return toPage(result, PageRequest.of(page, 5));
    }

    @Override
    @SneakyThrows
    public PageResumeDto resumeEditGet(Long id, Authentication auth) {
        if (!resumeDao.idIsExists(id)) {
            log.info("Было запрошено несуществующее резюме с ID " + id);
            throw new ResumeException("Резюме с " + id + " не существует!");
        }
        Resume resume = resumeDao.getResumeById(id).get();
        if (!Objects.equals(resume.getApplicantId(), userDao.getUserByEmail(auth.getName()).get().getId())) {
            log.info("Была попытка отредактировать чужое резюме");
            throw new ResumeException("Нельзя отредактировать чужое резюме");
        }
        return PageResumeDto.builder()
                .id(resume.getId())
                .salary(resume.getSalary())
                .name(resume.getName())
                .build();
    }

    @Override
    @SneakyThrows
    public Long resumeEditPost(UpdatePageResumeDto resumeDto, HttpServletRequest request, Authentication auth) {
        Long id = resumeDto.getId();
        if (!resumeDao.idIsExists(id)) {
            log.info("Была попытка отредактировать несуществующее резюме");
            throw new ResumeException("Резюме с ID " + id + " не существует!");
        }
        Resume resume = resumeDao.getResumeById(id).get();
        if (!Objects.equals(resume.getApplicantId(), userDao.getUserByEmail(auth.getName()).get().getId())) {
            log.info("Была попытка отредактировать чужое резюме");
            throw new ResumeException("Нельзя отредактировать чужое резюме!");
        }
        String isActive = request.getParameter("isActive");
        Resume updateRes = Resume.builder()
                .name(resumeDto.getName())
                .categoryId(resumeDto.getCategoryId())
                .salary(resumeDto.getSalary())
                .isActive("on".equals(isActive))
                .updateTime(LocalDateTime.now())
                .build();
        resumeDao.editResume(updateRes, id);
        return id;
    }

    @Override
    public Integer getCount() {
        return resumeDao.getCount();
    }

    private List<ResumeDto> getResumeDtos(List<Resume> resumes) {
        List<ResumeDto> resumeDtos = new ArrayList<>();
        for (Resume rs : resumes) {
            resumeDtos.add(
                    ResumeDto.builder()
                            .applicantId(rs.getApplicantId())
                            .name(rs.getName())
                            .categoryId(rs.getCategoryId())
                            .salary(rs.getSalary())
                            .isActive(rs.getIsActive())
                            .createdTime(rs.getCreatedTime())
                            .updateTime(rs.getUpdateTime())
                            .educationInfos(educationInfoService.getDtos(educationInfoDao.getEducationInfoByResumeId(rs.getId())))
                            .workExperienceInfos(workExperienceInfoService.getDtos(workExperienceInfoDao.getWorkExperienceByResumeId(rs.getId())))
                            .contactInfos(contactInfoService.getContactInfosByResumeId(rs.getId()))
                            .build()
            );
        }
        return resumeDtos;
    }
}
