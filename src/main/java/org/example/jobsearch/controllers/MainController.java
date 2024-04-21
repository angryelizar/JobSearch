package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.SendMessageDto;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.service.*;
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

    @GetMapping("messages")
    public String messagesGet(Model model, Authentication auth){
        model.addAttribute("pageTitle", "Сообщения");
        model.addAttribute("contacts", messageService.messagesGet(auth));
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
        return "main/message";
    }

    @PostMapping("/message/response")
    public String messagePost(@ModelAttribute  SendMessageDto messageDto, Model model, Authentication auth) {
        model.addAttribute("pageTitle", "Переписка");
        messageService.sendMessage(messageDto, auth);
        return "redirect:/message/response/" + messageDto.getRespondApplicant();
    }

    @GetMapping("/applicant/{id}")
    public String applicantGet(@PathVariable Long id, Model model){
        model.addAttribute("applicant", userService.getUserById(id));
        model.addAttribute("applicantId", id);
        model.addAttribute("resumes", resumeService.getPageResumesByAuthorId(id));
        model.addAttribute("pageTitle", "Профиль соискателя");
        return "main/applicant";
    }

    @GetMapping("/employer/{id}")
    public String employerGet(@PathVariable Long id, Model model){
        model.addAttribute("employer", userService.getUserById(id));
        model.addAttribute("employerId", id);
        model.addAttribute("vacancies", vacancyService.getPageVacanciesByAuthorId(id));
        model.addAttribute("pageTitle", "Профиль работодателя");
        return "main/employer";
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

    @GetMapping("/applicant/accept")
    public String acceptResponseEmployerGet(@RequestParam Long resume, @RequestParam Long vacancy, Authentication authentication){
        respondedApplicantsService.acceptResponse(resume, vacancy, authentication);
        return "redirect:/employer/responses";
    }

    @GetMapping("/applicant/deny")
    public String denyResponseEmployerGet(@RequestParam Long resume, @RequestParam Long vacancy, Authentication authentication){
        respondedApplicantsService.denyResponse(resume, vacancy, authentication);
        return "redirect:/employer/responses";
    }

    @GetMapping("/login")
    public String loginGet(Model model){
        model.addAttribute("pageTitle", "Вход в JobSearch");
        return "main/login";
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
