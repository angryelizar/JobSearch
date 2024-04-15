package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.service.ProfileService;
import org.example.jobsearch.service.RespondedApplicantsService;
import org.example.jobsearch.service.ResumeService;
import org.example.jobsearch.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final UserService userService;
    private final ProfileService profileService;
    private final RespondedApplicantsService respondedApplicantsService;
    private final ResumeService resumeService;

    @GetMapping
    public String homeGet() {
        return "redirect:/vacancies";
    }

    @GetMapping("/employers")
    public String employersGet(Model model) {
        model.addAttribute("pageTitle", "Работодатели");
        model.addAttribute("employers", userService.getEmployersUsers());
        return "main/employers";
    }

    @GetMapping("/registration")
    public String registrationGet(Model model) {
        model.addAttribute("pageTitle", "Регистрация");
        return "main/registration";
    }

    @GetMapping("/profile")
    public String profileGet(Model model, Authentication auth) throws UserNotFoundException {
        model.addAttribute("pageTitle", "Профиль");
        model.addAttribute("data", profileService.profileGet(auth));
        model.addAttribute("userId", userService.getFullUserByEmail(auth.getName()).getId());
        model.addAttribute("approvedNumber", respondedApplicantsService.getApprovedResponsesNumber(auth));
        return "main/profile";
    }

    @GetMapping("/applicant/{id}")
    public String applicantGet(@PathVariable Long id, Model model){
        model.addAttribute("applicant", userService.getUserById(id));
        model.addAttribute("applicantId", id);
        model.addAttribute("resumes", resumeService.getPageResumesByAuthorId(id));
        model.addAttribute("pageTitle", "Профиль соискателя");
        return "main/applicant";
    }

    @GetMapping("/applicant/responses")
    public String responseApplicantGet(Model model, Authentication authentication){
        model.addAttribute("pageTitle", "Отклики");
        model.addAttribute("responses", respondedApplicantsService.getApplicantResponsesByUser(authentication));
        return "main/applicant_responses";
    }

    @GetMapping("/employer/responses")
    public String responseEmployerGet(Model model, Authentication authentication){
        model.addAttribute("pageTitle", "Отклики");
        model.addAttribute("responses", respondedApplicantsService.getEmployerResponsesByUser(authentication));
        return "main/employer_responses";
    }

    @PostMapping("/profile")
    public String profilePost(UserDto userDto) {
        userService.update(userDto);
        return "redirect:/profile";
    }

    @PostMapping("/registration")
    public String registrationPost(UserDto userDto) {
        userService.createUser(userDto);
        return "redirect:/";
    }
}
