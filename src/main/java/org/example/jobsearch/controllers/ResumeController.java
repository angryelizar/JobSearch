package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.service.ResumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping()
    public String resumesGet(Model model){
        model.addAttribute("pageTitle", "Резюме");
        model.addAttribute("resumes", resumeService.getActivePageResumes());
        return "resume/resumes";
    }

    @GetMapping("/update")
    public String updateGet(@RequestParam Long id){
        resumeService.update(id);
        return "redirect:/profile";
    }
}
