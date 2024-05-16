package org.example.jobsearch.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.SendMessageDto;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.models.User;
import org.example.jobsearch.service.*;
import org.example.jobsearch.util.AuthenticatedUserProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

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
    private static final String AUTHENTICATED = "isAuthenticated";
    private static final String PAGE_TITLE = "pageTitle";
    private static final String IS_EMPLOYER = "isEmployer";

    @GetMapping
    public String homeGet() {
        return "redirect:/vacancies";
    }

    @GetMapping("/employers")
    public String employersGet(Model model, Pageable pageable) {
        model.addAttribute(PAGE_TITLE, "Работодатели");
        model.addAttribute("employers", userService.getEmployersUsers(pageable));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute("url", "employers");
        return "main/employers";
    }

    @GetMapping("/registration")
    public String registrationGet(Model model) {
        model.addAttribute("accountTypes", userService.getAccountTypes());
        model.addAttribute("userDto", new UserDto());
        model.addAttribute(PAGE_TITLE, "Регистрация");
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        return "main/registration";
    }

    @GetMapping("/forgot_password")
    public String forgotPasswordGet(Model model) {
        model.addAttribute(PAGE_TITLE, "Восстановление пароля");
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        return "main/forgot_password";
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        try {
            userService.getByResetPasswordToken(token);
            model.addAttribute("token", token);
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", "Неверный токен");
        }
        return "main/reset_password";
    }

    @GetMapping("/profile")
    public String profileGet(Model model, Authentication auth, Pageable pageable) throws UserNotFoundException {
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute(PAGE_TITLE, "Профиль");
        model.addAttribute("data", profileService.profileGet(auth, pageable));
        model.addAttribute("url", "profile");
        model.addAttribute("userId", userService.getFullUserByEmail(auth.getName()).getId());
        model.addAttribute("approvedNumber", respondedApplicantsService.getApprovedResponsesNumber(auth));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        return "main/profile";
    }

    @GetMapping("/login-failed")
    public String loginFailedGet(Model model) {
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute(PAGE_TITLE, "Вход не удался");
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        return "main/login-failed";
    }

    @GetMapping("messages")
    public String messagesGet(Model model, Authentication auth) {
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute(PAGE_TITLE, "Сообщения");
        model.addAttribute("contacts", messageService.messagesGet(auth));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        return "main/messages";
    }

    @GetMapping("/message/response/{id}")
    public String messageGet(@PathVariable Long id, Model model, Authentication auth) throws UserNotFoundException {
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute(PAGE_TITLE, "Переписка");
        model.addAttribute("employerName", respondedApplicantsService.getEmployerNameById(id));
        model.addAttribute("employerCountOfVacancies", respondedApplicantsService.getCountOfVacancies(id));
        model.addAttribute("applicantName", respondedApplicantsService.getApplicantNameById(id));
        model.addAttribute("applicantCountOfResumes", respondedApplicantsService.getCountOfResumes(id));
        model.addAttribute("messages", messageService.messageGetByRespondedApplicantId(id));
        model.addAttribute("fromTo", userService.getFullUserByEmail(auth.getName()).getId());
        model.addAttribute("toFrom", respondedApplicantsService.getRecipientId(id, userService.getFullUserByEmail(auth.getName()).getId()));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        return "main/message";
    }

    @PostMapping("/message/response")
    public String messagePost(@ModelAttribute SendMessageDto messageDto, Model model, Authentication auth) {
        model.addAttribute(PAGE_TITLE, "Переписка");
        messageService.sendMessage(messageDto, auth);
        return "redirect:/message/response/" + messageDto.getRespondApplicant();
    }

    @GetMapping("/applicant/{id}")
    public String applicantGet(@PathVariable Long id, Model model, Pageable pageable) {
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute("applicant", userService.getUserById(id));
        model.addAttribute("applicantId", id);
        model.addAttribute("resumes", resumeService.getPageResumesByAuthorId(id, pageable));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(PAGE_TITLE, "Профиль соискателя");
        model.addAttribute("url", "/applicant/" + id);
        return "main/applicant";
    }

    @GetMapping("/employer/{id}")
    public String employerGet(@PathVariable Long id, Model model, Pageable pageable) {
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute("employer", userService.getUserById(id));
        model.addAttribute("employerId", id);
        model.addAttribute("vacancies", vacancyService.getPageVacanciesByAuthorId(id, pageable));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute(PAGE_TITLE, "Профиль работодателя");
        model.addAttribute("url", "/employer/" + id);
        return "main/employer";
    }

    @GetMapping("/applicant/responses")
    public String responseApplicantGet(Model model, Authentication authentication) {
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute(PAGE_TITLE, "Отклики");
        model.addAttribute("responses", respondedApplicantsService.getApplicantResponsesByUser(authentication));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        return "main/applicant_responses";
    }

    @GetMapping("/employer/responses")
    public String responseEmployerGet(Model model, Authentication authentication) {
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute(PAGE_TITLE, "Отклики");
        model.addAttribute("responses", respondedApplicantsService.getEmployerResponsesByUser(authentication));
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
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
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        if (!isAuthenticated) {
            model.addAttribute(PAGE_TITLE, "Вход в JobSearch");
            model.addAttribute(AUTHENTICATED, isAuthenticated);
            return "main/login";
        }
        model.addAttribute(PAGE_TITLE, "Что-то пошло не так");
        model.addAttribute(AUTHENTICATED, isAuthenticated);
        return "redirect:/error/403";
    }

    @PostMapping("/profile")
    public String profilePost(UserDto userDto) {
        userService.update(userDto);
        return "redirect:/profile";
    }


    @PostMapping("/registration")
    public String registrationPost(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            userService.createUser(userDto);
            return "redirect:/login";
        }
        model.addAttribute(PAGE_TITLE, "Регистрация");
        model.addAttribute(IS_EMPLOYER, authenticatedUserProvider.isEmployer());
        model.addAttribute(AUTHENTICATED, authenticatedUserProvider.isAuthenticated());
        model.addAttribute("userDto", userDto);
        model.addAttribute("accountTypes", userService.getAccountTypes());
        return "main/registration";
    }

    @PostMapping("forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        try {
            userService.makeResetPasswordLink(request);
            model.addAttribute("message", "Мы отправили ссылку для сброса пароля на вашу электронную почту.");
        } catch (UsernameNotFoundException | UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "main/forgot_password";
    }

    @PostMapping("reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        try {
            User user = userService.getByResetPasswordToken(token);
            userService.updatePassword(user, password);
            model.addAttribute("message", "Вы успешно изменили ваш пароль.");
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("message", "Неверный токен");
        } catch (IllegalArgumentException e){
            model.addAttribute("message", e.getMessage());
        }
        return "main/reset_password_message";
    }
}
