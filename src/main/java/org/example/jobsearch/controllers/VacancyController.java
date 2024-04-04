package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.service.VacancyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vacancies")
public class VacancyController {
    private final VacancyService vacancyService;

    @GetMapping()
    public String vacanciesGet(Model model) {
        model.addAttribute("pageTitle", "Вакансии");
        model.addAttribute("vacancies", vacancyService.getActivePageVacancies());
        return "vacancy/vacancies";
    }

    @GetMapping("{id}")
    public String vacancyGet(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Вакансия");
        model.addAttribute("vacancy", vacancyService.getPageVacancyById(id));
        return "vacancy/vacancy";
    }

    @GetMapping("/update")
    public String updateGet(@RequestParam Long id) {
        vacancyService.update(id);
        return "redirect:/profile";
    }
}
