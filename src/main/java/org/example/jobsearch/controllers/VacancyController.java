package org.example.jobsearch.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.CreatePageVacancyDto;
import org.example.jobsearch.dto.UpdatePageVacancyDto;
import org.example.jobsearch.service.CategoryService;
import org.example.jobsearch.service.UserService;
import org.example.jobsearch.service.VacancyService;
import org.example.jobsearch.util.AuthenticatedUserProvider;
import org.springframework.data.domain.Pageable;
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
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private static final String AUTHENTICATED = "isAuthenticated";
    private static final String IS_EMPLOYER = "isEmployer";
    private static final String PAGE_TITLE = "pageTitle";
    private static final String CATEGORIES = "categories";
    private static final String REDIRECT_TO_PROFILE = "redirect:/profile";

    @GetMapping()
    public String vacanciesGet(Model model, Pageable pageable) {
        model.addAttribute(PAGE_TITLE, "Вакансии");
        model.addAttribute("url", "vacancies");
        model.addAttribute("page", vacancyService.getActivePageVacancies(pageable));
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "vacancy/vacancies";
    }

    @GetMapping("/search")
    public String searchGet(Model model) {
        model.addAttribute(PAGE_TITLE, "Поиск вакансий");
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "vacancy/search";
    }

    @GetMapping("/add")
    public String addGet(Model model) {
        model.addAttribute(PAGE_TITLE, "Создать вакансию");
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "vacancy/add";
    }

    @GetMapping("/{id}")
    public String vacancyGet(@PathVariable Long id, Model model, Authentication auth) {
        model.addAttribute(PAGE_TITLE, "Вакансия");
        model.addAttribute("vacancy", vacancyService.getPageVacancyById(id));
        model.addAttribute("isApplicant", userService.isApplicantByAuth(auth));
        model.addAttribute("employer", userService.getEmployerInfoByVacancyId(id));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute("userAuth", auth);
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute("isAuthor", vacancyService.isAuthor(id, auth));
        return "vacancy/vacancy";
    }

    @GetMapping("/edit/{id}")
    public String vacancyEditGet(@PathVariable Long id, Model model, Authentication authentication) {
        model.addAttribute(PAGE_TITLE, "Редактирование вакансии");
        model.addAttribute("vacancy", vacancyService.vacancyEditGet(id, authentication));
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "vacancy/edit";
    }

    @GetMapping("/update")
    public String updateGet(@RequestParam Long id) {
        vacancyService.update(id);
        return REDIRECT_TO_PROFILE;
    }

    @GetMapping("delete")
    public String delete(@RequestParam Long id, Authentication auth) {
        vacancyService.deleteVacancyById(id, auth);
        return REDIRECT_TO_PROFILE;
    }

    @GetMapping("filter")
    public String filterGet(@RequestParam(defaultValue = "0") Integer categoryId, @RequestParam(defaultValue = "createdDate") String criterion, @RequestParam(defaultValue = "increase") String order, Model model, Pageable pageable) {
        model.addAttribute(PAGE_TITLE, "Вакансии по фильтру");
        model.addAttribute("url", "/vacancies/filter/");
        model.addAttribute("page", vacancyService.getPageVacancyByFilter(categoryId, criterion, order, pageable));
        model.addAttribute(CATEGORIES, categoryService.getCategoriesList());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("criterion", criterion);
        model.addAttribute("order", order);
        return "vacancy/filtered";
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

    @PostMapping("/filter")
    public String filterPost(@RequestParam(defaultValue = "0") Integer categoryId, @RequestParam(defaultValue = "createdDate") String criterion, @RequestParam(defaultValue = "increase") String order, Model model) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("criterion", criterion);
        model.addAttribute("order", order);
        return String.format("redirect:/vacancies/filter?categoryId=%s&criterion=%s&order=%s", categoryId, criterion, order);
    }
}
