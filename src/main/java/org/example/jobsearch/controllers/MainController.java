package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.models.User;
import org.example.jobsearch.service.ProfileService;
import org.example.jobsearch.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    private final UserService userService;
    private final ProfileService profileService;

    @GetMapping
    public String homeGet(){
        return "redirect:/vacancies";
    }

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

    @PostMapping("/profile")
    public String profilePost(UserDto userDto){
        userService.update(userDto);
        return "redirect:/profile";
    }

    @PostMapping("/registration")
    public String registrationPost(UserDto userDto){
        userService.createUser(userDto);
        return "redirect:/";
    }
}
