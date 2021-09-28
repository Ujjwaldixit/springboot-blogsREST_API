package com.blogs.controller;

import com.blogs.model.Post;
import com.blogs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PostController {

    //display post in index.html

    @Autowired
    private PostService postService;
    @GetMapping("/")
    public String homePage(Model model)
    {
        model.addAttribute("posts",postService.getAllPosts());
        return "index";
    }
}
