package com.blogs.controller;

import com.blogs.model.User;
import com.blogs.service.UserService;
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

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("user")User user)
    {
        System.out.println(user.getName()+" "+user.getEmail()+" "+user.getPassword());
        userService.register(user);
        return "registrationForm";
    }

}
