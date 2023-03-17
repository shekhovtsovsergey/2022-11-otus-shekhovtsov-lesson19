package ru.otus.lesson27.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.lesson27.dto.UserDto;
import ru.otus.lesson27.model.User;
import ru.otus.lesson27.service.UserService;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/login/index")
    public String home(){
        return "index";
    }

    @GetMapping("/login/form")
    public String loginForm() {
        return "login-form";
    }

    @GetMapping("/login/register")
    public String showRegistrationForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/login/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model){
       return userService.registration(userDto,result,model);
    }
}
