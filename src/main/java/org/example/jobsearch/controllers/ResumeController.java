package org.example.jobsearch.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.CreatePageResumeDto;
import org.example.jobsearch.dto.UpdatePageResumeDto;
import org.example.jobsearch.service.*;
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
    private static final String PAGE_TITLE = "pageTitle";
    private static final String CATEGORIES = "categories";
    private final UserService userService;

    @GetMapping()
    public String resumesGet(Model model, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        model.addAttribute(PAGE_TITLE, "Резюме");
        model.addAttribute("size", resumeService.getCount());
        model.addAttribute("page", page);
        model.addAttribute("url", "resumes");
        model.addAttribute("resumes", resumeService.getActivePageResumes(page));
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        return "resume/resumes";
    }

    @GetMapping("/add")
    public String addGet(Model model) {
        model.addAttribute(PAGE_TITLE, "Создать резюме");
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        return "resume/add";
    }

    @GetMapping("/edit/{id}")
    public String editGet(@PathVariable Long id, Model model, Authentication auth) {
        model.addAttribute(PAGE_TITLE, "Отредактировать резюме");
        model.addAttribute("resume", resumeService.resumeEditGet(id, auth));
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        return "resume/edit";
    }

    @GetMapping("/{id}")
    public String resumeGet(@PathVariable Long id, Model model) {
        model.addAttribute(PAGE_TITLE, "Резюме");
        model.addAttribute("resume", resumeService.getPageResumeById(id));
        model.addAttribute("workExperience", workExperienceInfoService.getPageWorkExperienceByResumeId(id));
        model.addAttribute("educationInfo", educationInfoService.getPageEducationInfoByResumeId(id));
        model.addAttribute("contactInfo", contactInfoService.getPageContactInfoByResumeId(id));
        model.addAttribute("applicant", userService.getApplicantInfoByResumeId(id));
        return "resume/resume";
    }

    @GetMapping("/update")
    public String updateGet(@RequestParam Long id) {
        resumeService.update(id);
        return "redirect:/profile";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, Authentication auth) {
        resumeService.deleteResumeById(id, auth);
        return "redirect:/profile";
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
        return "redirect:/profile";
    }

    @PostMapping("/edit")
    public String editPost(UpdatePageResumeDto resumeDto, HttpServletRequest request, Authentication auth) {
        Long id = resumeService.resumeEditPost(resumeDto, request, auth);
        return String.format("redirect:/resumes/%d", id);
    }

    @PostMapping("/category")
    public String getByCategory(@RequestParam Integer categoryId, Model model, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        model.addAttribute(PAGE_TITLE, "Резюме");
        model.addAttribute("size", resumeService.getCount());
        model.addAttribute("resumes", resumeService.getPageResumeByCategoryId(Long.valueOf(categoryId), page));
        model.addAttribute("page", page);
        model.addAttribute("url", "resumes");
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        return "resume/resumes";
    }
}
