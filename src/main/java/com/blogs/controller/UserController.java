package com.blogs.controller;

import com.blogs.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/register")
    public String showRegistrationForm(Model model)
    {
        UserDto userDto=new UserDto();
        model.addAttribute("user",userDto);
        return "register";
    }
}
