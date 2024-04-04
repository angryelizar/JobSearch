package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.service.ContactInfoService;
import org.example.jobsearch.service.EducationInfoService;
import org.example.jobsearch.service.ResumeService;
import org.example.jobsearch.service.WorkExperienceInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {
    private final ResumeService resumeService;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final EducationInfoService educationInfoService;
    private final ContactInfoService contactInfoService;

    @GetMapping()
    public String resumesGet(Model model) {
        model.addAttribute("pageTitle", "Резюме");
        model.addAttribute("resumes", resumeService.getActivePageResumes());
        return "resume/resumes";
    }

    @GetMapping("{id}")
    public String resumeGet(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Резюме");
        model.addAttribute("resume", resumeService.getPageResumeById(id));
        model.addAttribute("workExperience", workExperienceInfoService.getPageWorkExperienceByResumeId(id));
        model.addAttribute("educationInfo", educationInfoService.getPageEducationInfoByResumeId(id));
        model.addAttribute("contactInfo", contactInfoService.getPageContactInfoByResumeId(id));
        return "resume/resume";
    }

    @GetMapping("/update")
    public String updateGet(@RequestParam Long id) {
        resumeService.update(id);
        return "redirect:/profile";
    }
}
