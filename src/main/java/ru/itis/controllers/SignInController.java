package ru.itis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itis.dto.SignInForm;
import ru.itis.dto.TokenDto;
import ru.itis.security.JwtUtils;
import ru.itis.services.interfaces.SignInService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignInController {
    private final SignInService signInService;
    private final JwtUtils jwtUtils;

    public SignInController(SignInService signInService, JwtUtils jwtUtils) {
        this.signInService = signInService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/signIn")
    public String getPage(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        boolean isAuthenticated = jwtUtils.validateToken(servletRequest, servletResponse);
        if (isAuthenticated) {
            return "redirect:/rooms";
        }
        return "signIn";
    }

    @PostMapping("/signIn")
    public String signIn(SignInForm signInForm, HttpServletResponse servletResponse) {
        TokenDto tokenDto = signInService.signIn(signInForm);
        if (tokenDto != null) {
            Cookie tokenCookie = new Cookie("X-Authorization", "Bearer_" + tokenDto.getToken());
            servletResponse.addCookie(tokenCookie);
            return "redirect:/";
        } else {
            return null;
        }
    }
}
