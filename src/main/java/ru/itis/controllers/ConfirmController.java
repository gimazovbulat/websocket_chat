package ru.itis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.dto.UserDto;
import ru.itis.models.UserState;
import ru.itis.services.interfaces.ConfirmService;

@Controller
public class ConfirmController {
    private final ConfirmService confirmService;

    public ConfirmController(ConfirmService confirmService) {
        this.confirmService = confirmService;
    }

    @GetMapping("/confirm/{link}")
    public String handleRequest(@PathVariable String link) {
        UserDto userDto = confirmService.confirm(link);
        if (userDto.getUserState() == UserState.CONFIRMED) {
            return "redirect:/signIn";
        } else {
            return "error";
        }
    }
}
 