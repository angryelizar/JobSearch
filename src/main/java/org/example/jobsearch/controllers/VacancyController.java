package org.example.jobsearch.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.CreatePageVacancyDto;
import org.example.jobsearch.dto.UpdatePageVacancyDto;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.service.CategoryService;
import org.example.jobsearch.service.UserService;
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
    private final UserService userService;

    @GetMapping()
    public String vacanciesGet(Model model, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        model.addAttribute("pageTitle", "Вакансии");
        model.addAttribute("url", "vacancies");
        model.addAttribute("size", vacancyService.getCount());
        model.addAttribute("page", page);
        model.addAttribute("vacancies", vacancyService.getActivePageVacancies(page));
        model.addAttribute("categories", categoryService.getCategoriesList());
        return "vacancy/vacancies";
    }

    @GetMapping("/add")
    public String addGet(Model model) {
        model.addAttribute("pageTitle", "Создать вакансию");
        model.addAttribute("categories", categoryService.getCategoriesList());
        return "vacancy/add";
    }

    @GetMapping("/{id}")
    public String vacancyGet(@PathVariable Long id, Model model, Authentication auth) throws UserNotFoundException {
        model.addAttribute("pageTitle", "Вакансия");
        model.addAttribute("vacancy", vacancyService.getPageVacancyById(id));
        model.addAttribute("isApplicant", userService.isApplicantByAuth(auth));
        model.addAttribute("userAuth", auth);
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
    public String delete(@RequestParam Long id, Authentication auth) {
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

    @PostMapping("/category")
    public String getByCategory(@RequestParam Integer categoryId, Model model, @RequestParam(name = "page", defaultValue = "0") Integer page) {
        model.addAttribute("pageTitle", "Вакансии");
        model.addAttribute("url", "vacancies");
        model.addAttribute("vacancies", vacancyService.getPageVacancyByCategoryId(Long.valueOf(categoryId), page));
        model.addAttribute("categories", categoryService.getCategoriesList());
        model.addAttribute("size", vacancyService.getCount());
        model.addAttribute("page", page);
        return "vacancy/vacancies";
    }
}
