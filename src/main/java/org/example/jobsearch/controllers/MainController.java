package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("registration")
    public String registrationGet(Model model){
        model.addAttribute("pageTitle", "Регистрация");
        return "main/registration";
    }
}
