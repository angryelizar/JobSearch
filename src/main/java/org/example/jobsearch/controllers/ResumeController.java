package org.example.jobsearch.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.CreatePageResumeDto;
import org.example.jobsearch.dto.UpdatePageResumeDto;
import org.example.jobsearch.service.*;
import org.example.jobsearch.util.AuthenticatedUserProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {
    private final ResumeService resumeService;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final EducationInfoService educationInfoService;
    private final ContactInfoService contactInfoService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private static final String AUTHENTICATED = "isAuthenticated";
    private static final String IS_EMPLOYER = "isEmployer";
    private static final String PAGE_TITLE = "pageTitle";
    private static final String CATEGORIES = "categories";
    private static final String REDIRECT_TO_PROFILE = "redirect:/profile";
    private static final String RESUME = "Резюме";

    @GetMapping()
    public String resumesGet(Model model, Pageable pageable) {
        model.addAttribute(PAGE_TITLE, RESUME);
        model.addAttribute("url", "resumes");
        model.addAttribute("page", resumeService.getActivePageResumes(pageable));
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "resume/resumes";
    }

    @GetMapping("/add")
    public String addGet(Model model) {
        model.addAttribute(PAGE_TITLE, "Создать резюме");
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "resume/add";
    }

    @GetMapping("/edit/{id}")
    public String editGet(@PathVariable Long id, Model model, Authentication auth) {
        model.addAttribute(PAGE_TITLE, "Отредактировать резюме");
        model.addAttribute("resume", resumeService.resumeEditGet(id, auth));
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "resume/edit";
    }

    @GetMapping("/{id}")
    public String resumeGet(@PathVariable Long id, Model model) {
        model.addAttribute(PAGE_TITLE, RESUME);
        model.addAttribute("resume", resumeService.getPageResumeById(id));
        model.addAttribute("workExperience", workExperienceInfoService.getPageWorkExperienceByResumeId(id));
        model.addAttribute("educationInfo", educationInfoService.getPageEducationInfoByResumeId(id));
        model.addAttribute("contactInfo", contactInfoService.getPageContactInfoByResumeId(id));
        model.addAttribute("applicant", userService.getApplicantInfoByResumeId(id));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute("isPermitted", resumeService.resumeShowPermitted(id, authenticatedUserProvider.getAuthenticatedUser()));
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "resume/resume";
    }

    @GetMapping("/update")
    public String updateGet(@RequestParam Long id) {
        resumeService.update(id);
        return REDIRECT_TO_PROFILE;
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Authentication auth) {
        resumeService.deleteResumeById(id, auth);
        return REDIRECT_TO_PROFILE;
    }

    @PostMapping("/add")
    public String addPost(CreatePageResumeDto pageResumeDto,
                          HttpServletRequest request,
                          Authentication auth,
                          @RequestParam(name = "telegram") String telegram,
                          @RequestParam(name = "whatsapp") String whatsapp,
                          @RequestParam(name = "telephone") String telephone,
                          @RequestParam(name = "linkedin") String linkedin,
                          @RequestParam(name = "email") String email
    ) {
        resumeService.addResumeFromForm(pageResumeDto, request, auth, telegram, whatsapp, telephone, linkedin, email);
        return REDIRECT_TO_PROFILE;
    }

    @PostMapping("/edit")
    public String editPost(UpdatePageResumeDto resumeDto, HttpServletRequest request, Authentication auth) {
        Long id = resumeService.resumeEditPost(resumeDto, request, auth);
        return String.format("redirect:/resumes/%d", id);
    }

    @PostMapping("/category")
    public String getByCategory(@RequestParam Integer categoryId, Model model, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        model.addAttribute(PAGE_TITLE, RESUME);
        model.addAttribute("size", resumeService.getCount());
        model.addAttribute("page", resumeService.getPageResumeByCategoryId(Long.valueOf(categoryId), page));
        model.addAttribute("url", "resumes");
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        return "resume/resumes";
    }
}
