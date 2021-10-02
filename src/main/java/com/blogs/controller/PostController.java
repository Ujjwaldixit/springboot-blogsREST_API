package com.blogs.controller;

import com.blogs.model.Post;
import com.blogs.model.Tag;
import com.blogs.service.PostService;
import com.blogs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class PostController {

    //display post in index.html

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;


//    @GetMapping("/page/{pageNo}")
//    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
//        int pageSize = 10;
//
////       // Page<Post> page = postService.findPaginated(pageNo, pageSize);
////        List<Post> posts = page.getContent();
////        model.addAttribute("currentPage", pageNo);
////        model.addAttribute("totalPages", page.getTotalPages());
////        model.addAttribute("totalItems", page.getTotalElements());
////        model.addAttribute("posts", posts);
////        return "index";
//    }
    @GetMapping("/")
    public String homePage(Model model,
                           @RequestParam(name="start",defaultValue = "0",required = false) int pageNo,
                           @RequestParam(name="limit",defaultValue ="10",required = false) int pageSize,
                           @RequestParam(name="sortField",defaultValue = "id",required = false)String sortBy,
                           @RequestParam(name="order",defaultValue="null",required = false) String order
    )
    {
        System.out.println("order="+order);
        Page<Post> page=postService.getAllPosts(pageNo,pageSize,sortBy,order);
        List<Post> posts=page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("posts", posts);
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
    public String savePost(@ModelAttribute("post")Post post,@RequestParam("tagName")String tagName,Tag tag)
    {
        postService.savePost(post);
        tag.setName(tagName);
        tagService.saveTag(tag);
        return "redirect:/";
    }
}
