package com.blogs.controller;

import com.blogs.model.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostController {
    @GetMapping("/")
    public String home()
    {
        System.out.println("Index");
        return "index";
    }

//    @GetMapping("/newpost")
//    public String getPosts(Model model)
//    {
//        model.addAttribute("post",new Post());
//        return "newposts";
//    }
}
