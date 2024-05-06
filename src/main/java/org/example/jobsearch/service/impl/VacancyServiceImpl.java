package org.example.jobsearch.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.*;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.models.*;
import org.example.jobsearch.repositories.*;
import org.example.jobsearch.service.CategoryService;
import org.example.jobsearch.service.ResumeService;
import org.example.jobsearch.service.UserService;
import org.example.jobsearch.service.VacancyService;
import org.example.jobsearch.util.DateUtil;
import org.example.jobsearch.util.ToPageUtil;
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
public class VacancyServiceImpl implements VacancyService {
    private final WorkExperienceInfoRepository workExperienceInfoRepository;
    private final UserService userService;
    private final ResumeService resumeService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;
    private final ResumeRepository resumeRepository;
    private final EducationInfoRepository educationInfoRepository;
    private final RespondedApplicantRepository respondedApplicantRepository;

    @Override
    public Integer getCount(){
        return vacancyRepository.findAll().size();
    }

    @Override
    public List<VacancyDto> getVacancies() {
        List<Vacancy> vacancies = vacancyRepository.findAll();
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<VacancyDto> getActiveVacancies() {
        List<Vacancy> vacancies = vacancyRepository.searchVacanciesByIsActiveEquals(true);
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<VacancyDto> getInActiveVacancies() {
        List<Vacancy> vacancies = vacancyRepository.searchVacanciesByIsActiveEquals(false);
        return getVacancyDtos(vacancies);
    }

    @Override
    public List<VacancyDto> getVacanciesByCategoryId(Long id) throws VacancyNotFoundException {
        List<Vacancy> vacancies = vacancyRepository.getVacanciesByCategoryId(id);
        if (vacancies.isEmpty()) {
            throw new VacancyNotFoundException("Вакансий в данной категории не найдено");
        }
        return getVacancyDtos(vacancies);
    }

    @Override
    @SneakyThrows
    public void createVacancy(Authentication auth, VacancyDto vacancyDto) {
        if (Boolean.FALSE.equals(categoryService.isExistsById(vacancyDto.getCategoryId()))) {
            throw new VacancyException("Выбранной категории не существует");
        }
        if (vacancyDto.getSalary() <= 0) {
            throw new VacancyException("Зарплата не может быть меньше или равна нулю!");
        }
        if (vacancyDto.getExpFrom() > vacancyDto.getExpTo()) {
            throw new VacancyException("Стартовый опыт работы не может быть больше окончательного!");
        }
        Vacancy vacancy = Vacancy.builder()
                .name(vacancyDto.getName())
                .description(vacancyDto.getDescription())
                .category(categoryRepository.findById(vacancyDto.getCategoryId()).get())
                .salary(vacancyDto.getSalary())
                .expFrom(vacancyDto.getExpFrom())
                .expTo(vacancyDto.getExpTo())
                .isActive(vacancyDto.getIsActive())
                .author(userRepository.getUserByEmail(auth.getName()).get())
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        vacancyRepository.save(vacancy);
    }

    @Override
    public VacancyDto getVacancyById(Long id) throws VacancyNotFoundException {
        if (vacancyRepository.findById(id).isEmpty()) {
            throw new VacancyNotFoundException("Вакансия не найдена");
        }
        return getVacancyDto(vacancyRepository.findById(id).get());
    }

    @Override
    @SneakyThrows
    public void editVacancy(Long id, UpdateVacancyDto updateVacancyDto) {
        if (!vacancyRepository.existsById(id)) {
            throw new VacancyException("Такой вакансии нет - нечего редактировать!");
        }
        if (Boolean.FALSE.equals(categoryService.isExistsById(updateVacancyDto.getCategoryId()))) {
            throw new VacancyException("Выбранной категории не существует");
        }
        if (updateVacancyDto.getSalary() <= 0) {
            throw new VacancyException("Зарплата не может быть меньше или равна нулю!");
        }
        if (updateVacancyDto.getExpFrom() > updateVacancyDto.getExpTo()) {
            throw new VacancyException("Стартовый опыт работы не может быть больше окончательного!");
        }
        Vacancy vacancy = Vacancy.builder()
                .name(updateVacancyDto.getName())
                .description(updateVacancyDto.getDescription())
                .category(categoryRepository.findById(updateVacancyDto.getCategoryId()).get())
                .salary(updateVacancyDto.getSalary())
                .expFrom(updateVacancyDto.getExpFrom())
                .expTo(updateVacancyDto.getExpTo())
                .isActive(updateVacancyDto.getIsActive())
                .updateTime(LocalDateTime.now())
                .build();
        vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteVacancyById(Long id) {
        vacancyRepository.deleteById(id);
    }

    @Override
    @SneakyThrows
    public void deleteVacancyById(Long id, Authentication authentication) {
        if (!vacancyRepository.existsById(id)){
            log.error("Вакансии с ID " + id + " не существует");
            throw new VacancyException("Такой вакансии нет");
        }
        Vacancy vacancy = vacancyRepository.findById(id).get();
        if (!Objects.equals(vacancy.getAuthor().getId(), userRepository.getUserByEmail(authentication.getName()).get().getId())){
            log.error("Была попытка удалить чужую вакансию");
            throw new VacancyException("Это не ваша вакансия!");
        }
        vacancyRepository.deleteById(id);
    }

    @Override
    public List<RespondedResumeDto> getRespondedResumesByVacancyId(Long id) {
        List<RespondApplicant> respondedApplicants = new ArrayList<>(respondedApplicantRepository.getRespondedApplicantsByVacancyId(id));
        List<RespondedResumeDto> respondedResumeDtos = new ArrayList<>();
        for (RespondApplicant respondedApplicant : respondedApplicants) {
            Resume resume = respondedApplicant.getResume();
            User applicant = resume.getApplicant();
            respondedResumeDtos.add(
                    RespondedResumeDto.builder()
                            .name(resume.getName())
                            .applicantName(applicant.getName())
                            .applicantSurname(applicant.getSurname())
                            .category(resume.getCategory().getName())
                            .salary(resume.getSalary())
                            .isActive(resume.getIsActive())
                            .createdTime(resume.getCreatedTime())
                            .updateTime(resume.getUpdateTime())
                            .educationInfos(educationInfoRepository.educationInfoByResumeId(resume.getId()))
                            .workExperienceInfos(workExperienceInfoRepository.findByResumeId(resume.getId()))
                            .build()
            );
        }
        return respondedResumeDtos;
    }

    @Override
    public List<VacancyDto> getVacanciesByQuery(String query) {
        return getVacancyDtos(vacancyRepository.getVacanciesByQuery(query));
    }

    @Override
    @SneakyThrows
    public void respondToVacancy(RespondedApplicantDto respondedApplicantDto) {
        if (!resumeRepository.existsById(respondedApplicantDto.getResumeId())) {
            throw new ResumeException("Резюме не существует!");
        }
        if (!vacancyRepository.existsById(respondedApplicantDto.getVacancyId())) {
            throw new VacancyException("Вакансии не существует!");
        }
        if (respondedApplicantRepository.isExists(respondedApplicantDto.getResumeId(), respondedApplicantDto.getVacancyId()) > 0) {
            throw new VacancyException("Соискатель уже откликался на эту вакансию!");
        }
        respondedApplicantRepository.save(RespondApplicant.builder()
                        .resume(resumeRepository.findById(respondedApplicantDto.getResumeId()).get())
                        .vacancy(vacancyRepository.findById(respondedApplicantDto.getVacancyId()).get())
                .build());
    }

    @Override
    public List<ProfileAndVacancyDto> getVacanciesByEmployerName(String employer) {
        List<User> users = new ArrayList<>(userRepository.findByName(employer));
        List<ProfileAndVacancyDto> vacAndUsers = new ArrayList<>();
        for (User currUsr : users) {
            vacAndUsers.add(ProfileAndVacancyDto.builder()
                    .name(currUsr.getName())
                    .surname(currUsr.getSurname())
                    .vacancyDtos(getVacancyDtos(vacancyRepository.findVacanciesByAuthorId(currUsr.getId())))
                    .build());
        }
        return vacAndUsers;
    }

    @Override
    public List<Vacancy> getVacanciesByEmployerId(Long id) {
        return vacancyRepository.findVacanciesByAuthorId(id);
    }

    @Override
    @SneakyThrows
    public void update(Long id) {
        if (vacancyRepository.existsById(id)) {
            Vacancy vacancy = vacancyRepository.findById(id).get();
            vacancy.setUpdateTime(LocalDateTime.now());
            vacancyRepository.save(vacancy);
        } else {
            log.error("Была запрошена несуществующая вакансия с ID " + id);
            throw new VacancyException("Такой вакансии нет!");
        }
    }

    @Override
    public List<PageVacancyDto> getActivePageVacancies() {
        List<Vacancy> vacancies = vacancyRepository.searchVacanciesByIsActiveEquals(true);
        vacancies.sort(Comparator.comparing(Vacancy::getUpdateTime).reversed());
        List<PageVacancyDto> pageVacancyDtos = new ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            User author = vacancy.getAuthor();
            pageVacancyDtos.add(
                    PageVacancyDto
                            .builder()
                            .id(vacancy.getId())
                            .name(vacancy.getName())
                            .description(vacancy.getDescription())
                            .category(vacancy.getCategory().getName())
                            .author(author.getName() + " " + author.getSurname())
                            .salary(vacancy.getSalary())
                            .expFrom(vacancy.getExpFrom())
                            .expTo(vacancy.getExpTo())
                            .updateTime(DateUtil.getFormattedLocalDateTime(vacancy.getUpdateTime()))
                            .build()
            );
        }
        return pageVacancyDtos;
    }

    @Override
    public Page<PageVacancyDto> getActivePageVacancies(Pageable pageable) {
        List<Vacancy> vacancies = vacancyRepository.searchVacanciesByIsActiveEquals(true);
        vacancies.sort(Comparator.comparing(Vacancy::getUpdateTime).reversed());
        List<PageVacancyDto> pageVacancyDtos = new ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            pageVacancyDtos.add(getPageVacancyById(vacancy.getId()));
        }
        return toPage(pageVacancyDtos, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    private Page<PageVacancyDto> toPage(List<PageVacancyDto> vacancies, Pageable pageable) {
        if (pageable.getOffset() >= vacancies.size()){
            log.error("Я пока не понял как эту ситуацию обработать....");
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize() > vacancies.size() ? vacancies.size() : pageable.getOffset() + pageable.getPageSize()));
        List<PageVacancyDto> subList = vacancies.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, vacancies.size());
    }

    @Override
    @SneakyThrows
    public PageVacancyDto getPageVacancyById(Long id) {
        if (vacancyRepository.existsById(id)) {
            Vacancy vacancy = vacancyRepository.findById(id).get();
            User author = vacancy.getAuthor();
            return PageVacancyDto
                    .builder()
                    .id(vacancy.getId())
                    .name(vacancy.getName())
                    .description(vacancy.getDescription())
                    .author(author.getName() + " " + author.getSurname())
                    .category(vacancy.getCategory().getName())
                    .salary(vacancy.getSalary())
                    .expFrom(vacancy.getExpFrom())
                    .expTo(vacancy.getExpTo())
                    .updateTime(DateUtil.getFormattedLocalDateTime(vacancy.getUpdateTime()))
                    .build();
        } else {
            log.error("Была запрошена несуществующая вакансия с ID " + id);
            throw new VacancyException("Такой вакансии нет!");
        }
    }

    @Override
    public Long addVacancyFromForm(CreatePageVacancyDto vacancyPageDto, HttpServletRequest request, Authentication auth) {
        String isActive = request.getParameter("isActive");
        vacancyPageDto.setIsActive("on".equals(isActive));
        User author = userRepository.getUserByEmail(auth.getName()).get();

        return vacancyRepository.save(Vacancy.builder()
                .name(vacancyPageDto.getName())
                .description(vacancyPageDto.getDescription())
                .category(categoryRepository.findById(vacancyPageDto.getCategoryId()).get())
                .salary(vacancyPageDto.getSalary())
                .expTo(vacancyPageDto.getExpTo())
                .expFrom(vacancyPageDto.getExpFrom())
                .author(author)
                .isActive(vacancyPageDto.getIsActive())
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build()).getId();
    }

    @Override
    @SneakyThrows
    public PageVacancyDto vacancyEditGet(Long id, Authentication authentication) {
        if (!vacancyRepository.existsById(id)){
            log.error("Вакансии с ID " + id + " не существует");
            throw new VacancyException("Такой вакансии нет");
        }
        Vacancy vacancy = vacancyRepository.findById(id).get();
        if (!Objects.equals(vacancy.getAuthor().getId(), userRepository.getUserByEmail(authentication.getName()).get().getId())){
            log.error("Была попытка отредактировать чужую вакансию");
            throw new VacancyException("Это не ваша вакансия!");
        }
        return PageVacancyDto
                .builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .salary(vacancy.getSalary())
                .expTo(vacancy.getExpTo())
                .expFrom(vacancy.getExpFrom())
                .build();
    }

    @Override
    @SneakyThrows
    public Long editVacancyFromForm(UpdatePageVacancyDto vacancyDto, HttpServletRequest request, Authentication auth) {
        Long id = vacancyDto.getId();
        if (!vacancyRepository.existsById(id)){
            log.error("Вакансии с ID " + id + " не существует");
            throw new VacancyException("Такой вакансии нет");
        }
        Vacancy vacancy = vacancyRepository.findById(id).get();
        if (!Objects.equals(vacancy.getAuthor().getId(), userRepository.getUserByEmail(auth.getName()).get().getId())){
            log.error("Была попытка отредактировать чужую вакансию");
            throw new VacancyException("Это не ваша вакансия!");
        }
        String isActive = request.getParameter("isActive");
        Vacancy updatedVacancy = Vacancy.builder()
                .id(id)
                .author(vacancy.getAuthor())
                .name(vacancyDto.getName())
                .description(vacancyDto.getDescription())
                .category(categoryRepository.findById(vacancyDto.getCategoryId()).get())
                .salary(vacancyDto.getSalary())
                .expFrom(vacancyDto.getExpFrom())
                .expTo(vacancyDto.getExpTo())
                .isActive("on".equals(isActive))
                .createdTime(vacancy.getCreatedTime())
                .updateTime(LocalDateTime.now())
                .build();
        vacancyRepository.save(updatedVacancy);
        return id;
    }

    @Override
    @SneakyThrows
    public List<PageVacancyDto> getPageVacancyByCategoryId(Long categoryId) {
        if (Boolean.FALSE.equals(categoryRepository.existsById(categoryId))){
            throw new VacancyException("Такой категории нет!");
        }
        List<Vacancy> vacancies = vacancyRepository.searchVacanciesByIsActiveEquals(true);
        vacancies.sort(Comparator.comparing(Vacancy::getUpdateTime).reversed());
        List<PageVacancyDto> resultVacancies = new ArrayList<>();
        for (Vacancy curVac : vacancies) {
            User author = curVac.getAuthor();
            if (Objects.equals(curVac.getCategory().getId(), categoryId)) {
                resultVacancies.add(PageVacancyDto.builder()
                        .id(curVac.getId())
                        .name(curVac.getName())
                        .author(author.getName() + " " + author.getSurname())
                        .category(curVac.getCategory().getName())
                        .salary(curVac.getSalary())
                        .updateTime(DateUtil.getFormattedLocalDateTime(curVac.getUpdateTime()))
                        .build());
            }
        }
        return resultVacancies;
    }

    @Override
    public Page<PageVacancyDto> getPageVacancyByCategoryId(Long categoryId, Pageable pageable) {
        List<PageVacancyDto> result = getPageVacancyByCategoryId(categoryId);
        return toPage(result, pageable);
    }

    @Override
    public String getNameById(Long vacancyId) {
        return vacancyRepository.findById(vacancyId).get().getName();
    }

    @Override
    @SneakyThrows
    public List<AjaxResumeDto> getResumesForVacancy(ResumesForVacancyDto resumesForVacancyDto) {
        User user = userService.getFullUserByEmail(resumesForVacancyDto.getUserEmail());
        Long categoryId = getVacancyCategoryByVacancyId(resumesForVacancyDto.getVacancyId());
        List<Resume> userResumes = resumeService.getFullResumesByUserId(user.getId());
        List<AjaxResumeDto> result = new ArrayList<>();
        for (Resume curr : userResumes) {
            if (curr.getIsActive() && Objects.equals(curr.getCategory().getId(), categoryId)) {
                result.add(AjaxResumeDto.builder()
                        .resumeName(curr.getName())
                        .resumeId(curr.getId())
                        .build());
            }
        }
        return result;
    }

    @Override
    public Page<ProfilePageVacancyDto> getPageVacanciesByAuthorId(Long id, Pageable pageable) {
        List<Vacancy> vacancyList = vacancyRepository.getVacanciesByAuthorId(id);
        List<ProfilePageVacancyDto> result = new ArrayList<>();
        for (Vacancy cur : vacancyList) {
            result.add(ProfilePageVacancyDto.builder()
                    .id(cur.getId())
                    .name(cur.getName())
                    .salary(cur.getSalary())
                    .updateDate(DateUtil.getFormattedLocalDateTime(cur.getUpdateTime()))
                    .build());
        }
        return ToPageUtil.toPageVacancy(result, pageable);
    }



    @Override
    @SneakyThrows
    public Page<PageVacancyDto> getPageVacancyByFilter(Integer categoryId, String criterion, String order, Pageable pageable) {
        List<Vacancy> vacancies = new ArrayList<>();
        if (categoryId == 0){
            vacancies.addAll(vacancyRepository.searchVacanciesByIsActiveEquals(true));
        } else {
            vacancies.addAll(vacancyRepository.getVacanciesByCategoryId(Long.valueOf(categoryId)));
        }
        vacancies.size();
        List<PageVacancyDto> resultVacancies = new ArrayList<>();
        resultVacancies.addAll(getPageVacancyDtos(vacancies));
        if (criterion.equalsIgnoreCase("createdDate")){
            resultVacancies.sort(Comparator.comparing(PageVacancyDto::getCreatedTime));
        } else if (criterion.equalsIgnoreCase("responseCount")){
            resultVacancies.sort(Comparator.comparing(PageVacancyDto::getCountOfResponses));
        }
        if (order.equalsIgnoreCase("decrease")){
            Collections.reverse(resultVacancies);
        }
        return ToPageUtil.toPageVacancyDto(resultVacancies, pageable);
    }

    private Long getVacancyCategoryByVacancyId(Long vacancyId) {
        return vacancyRepository.findById(vacancyId).get().getCategory().getId();
    }

    private List<VacancyDto> getVacancyDtos(List<Vacancy> vacancies) {
        List<VacancyDto> vacancyDtos = new ArrayList<>();
        vacancies.forEach(e -> vacancyDtos.add(VacancyDto.builder()
                .name(e.getName())
                .description(e.getDescription())
                .categoryId(e.getId())
                .salary(e.getSalary())
                .expFrom(e.getExpFrom())
                .expTo(e.getExpTo())
                .isActive(e.getIsActive())
                .createdTime(e.getCreatedTime())
                .updateTime(e.getUpdateTime())
                .build()));
        return vacancyDtos;
    }

    private VacancyDto getVacancyDto(Vacancy vacancy) {
        return VacancyDto.builder()
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .categoryId(vacancy.getCategory().getId())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .createdTime(vacancy.getCreatedTime())
                .updateTime(vacancy.getUpdateTime())
                .build();
    }

    private List<PageVacancyDto> getPageVacancyDtos(List<Vacancy> vacancies) {
        List<PageVacancyDto> result = new ArrayList<>();
        for (Vacancy cur : vacancies) {
            result.add(
                    PageVacancyDto.builder()
                            .id(cur.getId())
                            .name(cur.getName())
                            .description(cur.getDescription())
                            .author(cur.getAuthor().getName())
                            .category(cur.getCategory().getName())
                            .salary(cur.getSalary())
                            .updateTime(DateUtil.getFormattedLocalDateTime(cur.getUpdateTime()))
                            .createdTime(cur.getCreatedTime())
                            .countOfResponses(respondedApplicantRepository.countRespondApplicantByVacancyId(cur.getId()))
                            .build()
            );
        }
        return result;
    }
}
