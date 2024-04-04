package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registrationGet(Model model){
        model.addAttribute("pageTitle", "Регистрация");
        return "main/registration";
    }

    @GetMapping("/profile")
    public String profileGet(Model model){
        model.addAttribute("pageTitle", "Профиль");
        return "main/profile";
    }

    @PostMapping("/registration")
    public String registrationPost(UserDto userDto){
        userService.createUser(userDto);
        return "redirect:/";
    }
}
