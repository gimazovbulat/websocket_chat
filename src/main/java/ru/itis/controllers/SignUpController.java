package ru.itis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.dto.SignUpForm;
import ru.itis.services.interfaces.SignUpService;

@Controller
public class SignUpController {
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @GetMapping("/signUp")
    public String getPage() {
        return "signUp";
    }

    @PostMapping("/signUp")
    public void handleRequest(SignUpForm signUpForm) {
        signUpService.signUp(signUpForm);
    }
}
