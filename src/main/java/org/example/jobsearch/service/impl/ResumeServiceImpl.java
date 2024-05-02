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
import org.example.jobsearch.repositories.*;
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
    private final WorkExperienceInfoService workExperienceInfoService;
    private final EducationInfoService educationInfoService;
    private final ContactInfoService contactInfoService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final EducationInfoRepository educationInfoRepository;
    private final WorkExperienceInfoRepository workExperienceInfoRepository;

    @Override
    public List<ResumeDto> getResumes() {
        List<Resume> resumes = resumeRepository.findAll();
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getActiveResumes() {
        List<Resume> resumes = resumeRepository.findByIsActive(true);
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getInActiveResumes() {
        List<Resume> resumes = resumeRepository.findByIsActive(false);
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByCategoryId(Long id) throws ResumeNotFoundException {
        List<Resume> resumes = resumeRepository.findByCategoryId(id);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Резюме в категории нет или категории не существует");
        }
        return getResumeDtos(resumes);
    }

    @Override
    public List<ResumeDto> getResumesByUserId(Long id) throws ResumeNotFoundException {
        List<Resume> resumes = resumeRepository.findByAuthorId(id);
        if (resumes.isEmpty()) {
            throw new ResumeNotFoundException("Пользователь с этим ID либо не публиковал резюме - либо его нет :(");
        }
        return getResumeDtos(resumes);
    }

    @Override
    public List<Resume> getFullResumesByUserId(Long id) {
        return resumeRepository.findByAuthorId(id);
    }

    @Override
    public List<ResumeDto> getResumesByName(String query) {
        List<Resume> resumes = resumeRepository.findByNameLike(query);
        return getResumeDtos(resumes);
    }

    @Override
    public List<PageResumeDto> getActivePageResumes() {
        List<PageResumeDto> pageResumeDtos = new ArrayList<>();
        List<Resume> resumes = resumeRepository.findByIsActive(true);
        resumes.sort(Comparator.comparing(Resume::getUpdateTime).reversed());
        for (Resume curRes : resumes) {
            User author = curRes.getApplicant();
            pageResumeDtos.add(PageResumeDto
                    .builder()
                    .id(curRes.getId())
                    .category(curRes.getCategory().getName())
                    .name(curRes.getName())
                    .author(author.getName() + " " + author.getSurname())
                    .updatedDate(DateUtil.getFormattedLocalDateTime(curRes.getUpdateTime()))
                    .salary(curRes.getSalary())
                    .build());
        }
        return pageResumeDtos;
    }

    @Override
    public Page<PageResumeDto> getActivePageResumes(Integer page) {
        List<PageResumeDto> resumes = getActivePageResumes();
        if (page < 0) {
            page = 0;
        }
        return toPage(resumes, PageRequest.of(page, 5));
    }

    private Page<PageResumeDto> toPage(List<PageResumeDto> resumes, Pageable pageable) {
        if (pageable.getOffset() >= resumes.size()) {
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
        List<User> users = new ArrayList<>(userRepository.findByName(user));
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
        User user = userRepository.findById(resumeDto.getApplicantId()).get();
        if (user.getAccountType().equalsIgnoreCase("Работодатель") || !userRepository.existsById(resumeDto.getApplicantId())) {
            throw new ResumeException("Пользователь либо работодатель, либо его не существует!");
        }
        if (Boolean.FALSE.equals(categoryRepository.existsById(resumeDto.getCategoryId()))) {
            throw new ResumeException("Выбранной вами категории не существует");
        }
        if (resumeDto.getSalary() <= 0) {
            throw new ResumeException("Зарплата не может быть меньше или равна нулю");
        }
        User applicant = userRepository.getUserByEmail(authentication.getName()).get();
        Long resumeId = resumeRepository.save(Resume.builder()
                .applicant(applicant)
                .name(resumeDto.getName())
                .category(categoryRepository.findById(resumeDto.getCategoryId()).get())
                .salary(resumeDto.getSalary())
                .isActive(resumeDto.getIsActive())
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build()).getId();
        Resume createdResume = resumeRepository.findById(resumeId).get();
        if (!resumeDto.getEducationInfos().isEmpty()) {
            resumeDto.getEducationInfos().forEach(e -> educationInfoRepository.save(
                    EducationInfo.builder()
                            .resume(createdResume)
                            .institution(e.getInstitution())
                            .program(e.getProgram())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .degree(e.getDegree())
                            .build()
            ));
        }
        if (!resumeDto.getWorkExperienceInfos().isEmpty()) {
            resumeDto.getWorkExperienceInfos().forEach(e -> workExperienceInfoRepository.save(
                    WorkExperienceInfo.builder()
                            .resume(createdResume)
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
        if (Boolean.FALSE.equals(categoryRepository.existsById(updateResumeDto.getCategoryId()))) {
            throw new ResumeException("Выбранной вами категории не существует");
        }
        if (updateResumeDto.getSalary() <= 0) {
            throw new ResumeException("Зарплата не может быть меньше или равна нулю");
        }
        Resume resume = resumeRepository.findById(id).get();
        resume.setName(updateResumeDto.getName());
        resume.setSalary(updateResumeDto.getSalary());
        resume.setCategory(categoryRepository.findById(updateResumeDto.getCategoryId()).get());
        resume.setIsActive(updateResumeDto.getIsActive());
        resume.setUpdateTime(LocalDateTime.now());
        resumeRepository.save(resume);
        if (!updateResumeDto.getEducationInfos().isEmpty()) {
            updateResumeDto.getEducationInfos().forEach(e -> educationInfoRepository.save(
                    EducationInfo.builder()
                            .resume(resume)
                            .institution(e.getInstitution())
                            .program(e.getProgram())
                            .startDate(e.getStartDate())
                            .endDate(e.getEndDate())
                            .degree(e.getDegree())
                            .build()
            ));
        }
        if (!updateResumeDto.getWorkExperienceInfos().isEmpty()) {
            updateResumeDto.getWorkExperienceInfos().forEach(e -> workExperienceInfoRepository.save(
                    WorkExperienceInfo.builder()
                            .resume(resume)
                            .years(e.getYears())
                            .companyName(e.getCompanyName())
                            .position(e.getPosition())
                            .responsibilities(e.getResponsibilities())
                            .build()
            ));
        }
    }

    @Override
    public void deleteResumeById(Long id) {
        resumeRepository.deleteById(id);
    }

    @Override
    @SneakyThrows
    public void deleteResumeById(Long id, Authentication auth) {
        if (!resumeRepository.existsById(id)) {
            log.info("Была попытка удалить несуществующую вакансию с ID " + id);
            throw new ResumeException("Такой вакансии нет! ID " + id);
        }
        Resume resume = resumeRepository.findById(id).get();
        if (!Objects.equals(resume.getApplicant().getId(), userRepository.getUserByEmail(auth.getName()).get().getId())) {
            log.info("Была попытка удалить чужую вакансию с ID " + id);
            throw new ResumeException("Это не ваше резюме - вы не можете его удалить!");
        }
        resumeRepository.deleteById(id);
    }

    @Override
    @SneakyThrows
    public void update(Long id) {
        if (resumeRepository.existsById(id)) {
            Resume resume = resumeRepository.findById(id).get();
            resume.setUpdateTime(LocalDateTime.now());
            resumeRepository.save(resume);
        } else {
            log.error("Было запрошено несуществующее резюме с ID " + id);
            throw new ResumeException("Этого резюме не существует");
        }
    }

    @Override
    @SneakyThrows
    public PageResumeDto getPageResumeById(Long id) {
        if (resumeRepository.existsById(id)) {
            Resume resume = resumeRepository.findById(id).get();
            return PageResumeDto.builder()
                    .name(resume.getName())
                    .category(resume.getCategory().getName())
                    .author(resume.getApplicant().getName() + " " + resume.getApplicant().getSurname())
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
                .applicant(userRepository.getUserByEmail(auth.getName()).get())
                .name(pageResumeDto.getName())
                .category(categoryRepository.findById(pageResumeDto.getCategoryId()).get())
                .salary(pageResumeDto.getSalary())
                .isActive(pageResumeDto.getIsActive())
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        Long resumeId = resumeRepository.save(resume).getId();
        Resume createdResume = resumeRepository.findById(resumeId).get();
        List<WorkExperienceInfoDto> workExperienceInfoDtos = pageResumeDto.getWorkExperienceInfos();
        List<EducationInfoDto> educationInfoDtos = pageResumeDto.getEducationInfos();
        if (!workExperienceInfoDtos.isEmpty()) {
            for (WorkExperienceInfoDto curDto : workExperienceInfoDtos) {
                if (!workExperienceInfoService.isValid(curDto)) {
                    log.error("Информация об опыте работы невалидна и не будет добавлена");
                    log.error(curDto.toString());
                } else if (workExperienceInfoService.isValid(curDto)) {
                    WorkExperienceInfo info = WorkExperienceInfo.builder()
                            .companyName(curDto.getCompanyName())
                            .resume(createdResume)
                            .responsibilities(curDto.getResponsibilities())
                            .position(curDto.getPosition())
                            .years(curDto.getYears())
                            .build();
                    workExperienceInfoRepository.save(info);
                }
            }
        }
        if (!educationInfoDtos.isEmpty()) {
            for (EducationInfoDto curDto : educationInfoDtos) {
                if (!educationInfoService.isValid(curDto)) {
                    log.error("Информация об обучении невалидна и не будет добавлена");
                    log.error(curDto.toString());
                } else if (educationInfoService.isValid(curDto)) {
                    EducationInfo educationInfo = EducationInfo.builder()
                            .resume(createdResume)
                            .startDate(curDto.getStartDate())
                            .endDate(curDto.getEndDate())
                            .institution(curDto.getInstitution())
                            .program(curDto.getProgram())
                            .degree(curDto.getDegree())
                            .build();
                    educationInfoRepository.save(educationInfo);
                }
            }
        }
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
        if (Boolean.FALSE.equals(categoryRepository.existsById(categoryId))) {
            throw new VacancyException("Такой категории нет!");
        }
        List<Resume> resumes = resumeRepository.findByIsActive(true);
        resumes.sort(Comparator.comparing(Resume::getUpdateTime).reversed());
        List<PageResumeDto> resumeDtos = new ArrayList<>();
        for (Resume curRes : resumes) {
            if (Objects.equals(curRes.getCategory().getId(), categoryId)) {
                resumeDtos.add(PageResumeDto.builder()
                        .id(curRes.getId())
                        .category(curRes.getCategory().getName())
                        .salary(curRes.getSalary())
                        .name(curRes.getName())
                        .author(curRes.getApplicant().getName() + " " + curRes.getApplicant().getSurname())
                        .updatedDate(DateUtil.getFormattedLocalDateTime(curRes.getUpdateTime()))
                        .build());
            }
        }
        return resumeDtos;
    }

    @Override
    public Page<PageResumeDto> getPageResumeByCategoryId(Long id, Integer page) {
        List<PageResumeDto> result = getPageResumeByCategoryId(id);
        if (page < 0) {
            page = 0;
        }
        return toPage(result, PageRequest.of(page, 5));
    }

    @Override
    @SneakyThrows
    public PageResumeDto resumeEditGet(Long id, Authentication auth) {
        if (!resumeRepository.existsById(id)) {
            log.info("Было запрошено несуществующее резюме с ID " + id);
            throw new ResumeException("Резюме с " + id + " не существует!");
        }
        Resume resume = resumeRepository.findById(id).get();
        if (!Objects.equals(resume.getApplicant().getId(), userRepository.getUserByEmail(auth.getName()).get().getId())) {
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
        if (!resumeRepository.existsById(id)) {
            log.info("Была попытка отредактировать несуществующее резюме");
            throw new ResumeException("Резюме с ID " + id + " не существует!");
        }
        Resume resume = resumeRepository.findById(id).get();
        if (!Objects.equals(resume.getApplicant().getId(), userRepository.getUserByEmail(auth.getName()).get().getId())) {
            log.info("Была попытка отредактировать чужое резюме");
            throw new ResumeException("Нельзя отредактировать чужое резюме!");
        }
        String isActive = request.getParameter("isActive");
        Resume updateRes = Resume.builder()
                .id(resume.getId())
                .name(resumeDto.getName())
                .applicant(resume.getApplicant())
                .category(categoryRepository.findById(resumeDto.getCategoryId()).get())
                .salary(resumeDto.getSalary())
                .isActive("on".equals(isActive))
                .createdTime(resume.getCreatedTime())
                .updateTime(LocalDateTime.now())
                .build();
        return resumeRepository.save(updateRes).getId();
    }

    @Override
    public Integer getCount() {
        return resumeRepository.findAll().size();
    }

    @Override
    public String getResumeNameById(Long id) {
        return resumeRepository.findById(id).get().getName();
    }

    @Override
    public User getAuthorByResumeId(Long id) {
        return resumeRepository.findById(id).get().getApplicant();
    }

    @Override
    public List<ProfilePageResumeDto> getPageResumesByAuthorId(Long id) {
        List<Resume> resumes = getFullResumesByUserId(id);
        List<ProfilePageResumeDto> pageResumeDtos = new ArrayList<>();
        resumes.sort(Comparator.comparing(Resume::getUpdateTime).reversed());
        for (Resume resume : resumes) {
            pageResumeDtos.add(
                    ProfilePageResumeDto.builder()
                            .id(resume.getId())
                            .name(resume.getName())
                            .salary(resume.getSalary())
                            .updateDate(DateUtil.getFormattedLocalDateTime(resume.getUpdateTime()))
                            .build()
            );
        }
        return pageResumeDtos;
    }

    @Override
    public Long getResumeCategoryByResumeId(Long resumeId) {
        return resumeRepository.findById(resumeId).get().getCategory().getId();
    }

    @Override
    @SneakyThrows
    public Boolean resumeShowPermitted(Long id, User authenticatedUser) {
        if (Objects.equals(authenticatedUser.getAccountType(), "Администратор") || "Работодатель".equals(authenticatedUser.getAccountType())) {
            return true;
        }
        Optional<Resume> maybeResume = resumeRepository.findById(id);
        if (maybeResume.isEmpty()) {
            throw new ResumeException("Такого резюме нет! ID " + id);
        }
        if (Objects.equals(maybeResume.get().getApplicant().getId(), authenticatedUser.getId())) {
            return true;
        }
        return false;
    }

    private List<ResumeDto> getResumeDtos(List<Resume> resumes) {
        List<ResumeDto> resumeDtos = new ArrayList<>();
        for (Resume rs : resumes) {
            resumeDtos.add(
                    ResumeDto.builder()
                            .applicantId(rs.getApplicant().getId())
                            .name(rs.getName())
                            .categoryId(rs.getCategory().getId())
                            .salary(rs.getSalary())
                            .isActive(rs.getIsActive())
                            .createdTime(rs.getCreatedTime())
                            .updateTime(rs.getUpdateTime())
                            .educationInfos(educationInfoService.getDtos(educationInfoRepository.educationInfoByResumeId(rs.getId())))
                            .workExperienceInfos(workExperienceInfoService.getDtos(workExperienceInfoRepository.findByResumeId(rs.getId())))
                            .contactInfos(contactInfoService.getContactInfosByResumeId(rs.getId()))
                            .build()
            );
        }
        return resumeDtos;
    }
}