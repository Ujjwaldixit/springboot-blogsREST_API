package com.blogs.controller;

import com.blogs.model.Post;
import com.blogs.model.Tag;
import com.blogs.service.PostService;
import com.blogs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@Controller
public class PostController {

    //display post in index.html

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

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

    //save post and tags to database
    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("post")Post post,@RequestParam("tagName")String tagName)
    {
        postService.savePost(post);
        Tag tag=new Tag();
        tag.setName(tagName);
        tagService.saveTag(tag);
        return "redirect:/";
    }



}
