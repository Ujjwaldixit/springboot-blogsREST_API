package com.blogs.controller;

import com.blogs.model.User;
import com.blogs.service.UserService;
import jdk.internal.icu.text.NormalizerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model)
    {
        model.addAttribute("user",new User());
        return "registrationForm";
    }

    @PostMapping
    public void registerUser(@ModelAttribute("user")User user)
    {
        userService.register(user);
    }

}
