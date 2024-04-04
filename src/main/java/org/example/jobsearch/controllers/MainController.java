package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.service.ProfileService;
import org.example.jobsearch.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final ProfileService profileService;

    @GetMapping("/registration")
    public String registrationGet(Model model){
        model.addAttribute("pageTitle", "Регистрация");
        return "main/registration";
    }

    @GetMapping("/profile")
    public String profileGet(Model model, Authentication auth){
        model.addAttribute("pageTitle", "Профиль");
        model.addAttribute("data", profileService.profileGet(auth));
        return "main/profile";
    }

    @PostMapping("/registration")
    public String registrationPost(UserDto userDto){
        userService.createUser(userDto);
        return "redirect:/";
    }
}
