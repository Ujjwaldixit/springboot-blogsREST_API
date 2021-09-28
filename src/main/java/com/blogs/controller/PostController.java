package com.blogs.controller;

import com.blogs.model.Post;
import com.blogs.model.Tag;
import com.blogs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.TableGenerator;

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
    @GetMapping("/showNewPostForm")
    public String newPost(Model model)
    {
        Post post=new Post();
        model.addAttribute("post",post);
        return "/newpost";
    }

    //save post to database
    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("post")Post post, @ModelAttribute("tag") Tag tag){

        postService.savePost(post);
        return "redirect:/";
    }

}
