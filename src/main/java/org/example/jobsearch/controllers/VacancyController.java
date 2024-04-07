package org.example.jobsearch.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.CreatePageVacancyDto;
import org.example.jobsearch.dto.UpdatePageVacancyDto;
import org.example.jobsearch.service.CategoryService;
import org.example.jobsearch.service.VacancyService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vacancies")
public class VacancyController {
    private final VacancyService vacancyService;
    private final CategoryService categoryService;

    @GetMapping()
    public String vacanciesGet(Model model) {
        model.addAttribute("pageTitle", "Вакансии");
        model.addAttribute("vacancies", vacancyService.getActivePageVacancies());
        return "vacancy/vacancies";
    }

    @GetMapping("/add")
    public String addGet(Model model) {
        model.addAttribute("pageTitle", "Создать вакансию");
        model.addAttribute("categories", categoryService.getCategoriesList());
        return "vacancy/add";
    }

    @GetMapping("/{id}")
    public String vacancyGet(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Вакансия");
        model.addAttribute("vacancy", vacancyService.getPageVacancyById(id));
        return "vacancy/vacancy";
    }

    @GetMapping("/edit/{id}")
    public String vacancyEditGet(@PathVariable Long id, Model model, Authentication authentication) {
        model.addAttribute("pageTitle", "Редактирование вакансии");
        model.addAttribute("vacancy", vacancyService.vacancyEditGet(id, authentication));
        model.addAttribute("categories", categoryService.getCategoriesList());
        return "vacancy/edit";
    }

    @GetMapping("/update")
    public String updateGet(@RequestParam Long id) {
        vacancyService.update(id);
        return "redirect:/profile";
    }

    @GetMapping("delete")
    public String delete(@RequestParam Long id, Authentication auth){
        vacancyService.deleteVacancyById(id, auth);
        return "redirect:/profile";
    }

    @PostMapping("/add")
    public String addPost(CreatePageVacancyDto vacancyPageDto, HttpServletRequest request, Authentication auth) {
        Long id = vacancyService.addVacancyFromForm(vacancyPageDto, request, auth);
        return String.format("redirect:/vacancies/%d", id);
    }

    @PostMapping("/edit")
    public String editPost(UpdatePageVacancyDto vacancyDto, HttpServletRequest request, Authentication auth) {
        Long id = vacancyService.editVacancyFromForm(vacancyDto, request, auth);
        return String.format("redirect:/vacancies/%d", id);
    }
}
