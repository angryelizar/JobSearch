package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.SendMessageDto;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.service.*;
import org.example.jobsearch.util.AuthenticatedUserProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final UserService userService;
    private final ProfileService profileService;
    private final RespondedApplicantsService respondedApplicantsService;
    private final ResumeService resumeService;
    private final VacancyService vacancyService;
    private final MessageService messageService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @GetMapping
    public String homeGet() {
        return "redirect:/vacancies";
    }

    @GetMapping("/employers")
    public String employersGet(Model model) {
        model.addAttribute("pageTitle", "Работодатели");
        model.addAttribute("employers", userService.getEmployersUsers());
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        return "main/employers";
    }

    @GetMapping("/registration")
    public String registrationGet(Model model) {
        model.addAttribute("pageTitle", "Регистрация");
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        return "main/registration";
    }

    @GetMapping("/profile")
    public String profileGet(Model model, Authentication auth, Pageable pageable) throws UserNotFoundException {
        model.addAttribute("pageTitle", "Профиль");
        model.addAttribute("data", profileService.profileGet(auth, pageable));
        model.addAttribute("url", "profile");
        model.addAttribute("userId", userService.getFullUserByEmail(auth.getName()).getId());
        model.addAttribute("approvedNumber", respondedApplicantsService.getApprovedResponsesNumber(auth));
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        return "main/profile";
    }

    @GetMapping("/login-failed")
    public String loginFailedGet(Model model) {
        model.addAttribute("pageTitle", "Вход не удался");
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        return "main/login-failed";
    }

    @GetMapping("messages")
    public String messagesGet(Model model, Authentication auth) {
        model.addAttribute("pageTitle", "Сообщения");
        model.addAttribute("contacts", messageService.messagesGet(auth));
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        return "main/messages";
    }

    @GetMapping("/message/response/{id}")
    public String messageGet(@PathVariable Long id, Model model, Authentication auth) throws UserNotFoundException {
        model.addAttribute("pageTitle", "Переписка");
        model.addAttribute("employerName", respondedApplicantsService.getEmployerNameById(id));
        model.addAttribute("employerCountOfVacancies", respondedApplicantsService.getCountOfVacancies(id));
        model.addAttribute("applicantName", respondedApplicantsService.getApplicantNameById(id));
        model.addAttribute("applicantCountOfResumes", respondedApplicantsService.getCountOfResumes(id));
        model.addAttribute("messages", messageService.messageGetByRespondedApplicantId(id));
        model.addAttribute("fromTo", userService.getFullUserByEmail(auth.getName()).getId());
        model.addAttribute("toFrom", respondedApplicantsService.getRecipientId(id, userService.getFullUserByEmail(auth.getName()).getId()));
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        return "main/message";
    }

    @PostMapping("/message/response")
    public String messagePost(@ModelAttribute SendMessageDto messageDto, Model model, Authentication auth) {
        model.addAttribute("pageTitle", "Переписка");
        messageService.sendMessage(messageDto, auth);
        return "redirect:/message/response/" + messageDto.getRespondApplicant();
    }

    @GetMapping("/applicant/{id}")
    public String applicantGet(@PathVariable Long id, Model model, Pageable pageable) {
        model.addAttribute("applicant", userService.getUserById(id));
        model.addAttribute("applicantId", id);
        model.addAttribute("resumes", resumeService.getPageResumesByAuthorId(id, pageable));
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        model.addAttribute("pageTitle", "Профиль соискателя");
        model.addAttribute("url", "/applicant/" + id);
        return "main/applicant";
    }

    @GetMapping("/employer/{id}")
    public String employerGet(@PathVariable Long id, Model model, Pageable pageable) {
        model.addAttribute("employer", userService.getUserById(id));
        model.addAttribute("employerId", id);
        model.addAttribute("vacancies", vacancyService.getPageVacanciesByAuthorId(id, pageable));
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        model.addAttribute("pageTitle", "Профиль работодателя");
        model.addAttribute("url", "/employer/" + id);
        return "main/employer";
    }

    @GetMapping("/applicant/responses")
    public String responseApplicantGet(Model model, Authentication authentication) {
        model.addAttribute("pageTitle", "Отклики");
        model.addAttribute("responses", respondedApplicantsService.getApplicantResponsesByUser(authentication));
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        return "main/applicant_responses";
    }

    @GetMapping("/employer/responses")
    public String responseEmployerGet(Model model, Authentication authentication) {
        model.addAttribute("pageTitle", "Отклики");
        model.addAttribute("responses", respondedApplicantsService.getEmployerResponsesByUser(authentication));
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        return "main/employer_responses";
    }

    @GetMapping("/applicant/accept")
    public String acceptResponseEmployerGet(@RequestParam Long resume, @RequestParam Long vacancy, Authentication authentication) {
        respondedApplicantsService.acceptResponse(resume, vacancy, authentication);
        return "redirect:/employer/responses";
    }

    @GetMapping("/applicant/deny")
    public String denyResponseEmployerGet(@RequestParam Long resume, @RequestParam Long vacancy, Authentication authentication) {
        respondedApplicantsService.denyResponse(resume, vacancy, authentication);
        return "redirect:/employer/responses";
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        boolean isAuthenticated = authenticatedUserProvider.isAuthenticated();
        if (!isAuthenticated) {
            model.addAttribute("pageTitle", "Вход в JobSearch");
            model.addAttribute("isAuthenticated", isAuthenticated);
            return "main/login";
        }
        model.addAttribute("pageTitle", "Что-то пошло не так");
        model.addAttribute("isAuthenticated", isAuthenticated);
        return "redirect:/error/403";
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
