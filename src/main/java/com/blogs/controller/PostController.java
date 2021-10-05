package com.blogs.controller;

import com.blogs.model.*;
import com.blogs.repository.PostAndTagRepository;
import com.blogs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Controller
public class PostController {

    //display post in index.html
    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;
    @Autowired
    private PostAndTagService postAndTagService;

    @GetMapping("/")
    public String homePage(Model model,
                           @RequestParam(name="start",defaultValue = "0",required = false) int pageNo,
                           @RequestParam(name="limit",defaultValue ="10",required = false) int pageSize,
                           @RequestParam(name="sortField",defaultValue = "id",required = false)String sortBy,
                           @RequestParam(name="order",defaultValue="null",required = false) String order
    )
    {
        //System.out.println("order="+order);
        Page<Post> page=postService.getAllPosts(pageNo,pageSize,sortBy,order);
        List<Post> posts=page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("posts", posts);
        return "index";
    }


    @GetMapping("/showNewPostForm")
    public String newPost(@AuthenticationPrincipal UserDetailsImpl user, Model model,Post post)
    {
        //to set author name
        post.setAuthor(user.getName());
        model.addAttribute("post",post);

        List<Tag> tags=tagService.getAllTags();
        model.addAttribute("tags",tags);
        return "/newPost";
    }

    //save post and tags to database
    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("post")Post post,@RequestParam("Tags") String tags, PostTag postTag)
    {
        System.out.println("post author ="+post.getAuthor());
        System.out.println("post="+post);
        int postId=postService.savePost(post);
        //save tags
        List<Integer> tagIds = null;
        if(!tags.equals("")) {
            tagIds = tagService.saveTag(tags);
        }
        System.out.println("postId"+postId+ "   tag id= "+tagIds);
        postTag.setPostId(postId);

        if(tagIds.size()>0) {
            for (int tagId : tagIds) {
                postTag.setTagId(tagId);
                postAndTagService.addPostTag(postTag);
            }
        }
        else{
            postTag.setTagId(0);
            postAndTagService.addPostTag(postTag);
        }
        return "redirect:/";
    }

    @GetMapping("/fullPost/{postId}")
    public String displayFullPost(@AuthenticationPrincipal UserDetailsImpl user,@PathVariable("postId") int id,Model model,@ModelAttribute("comment") Comment comment)
    {
        Post post=postService.findPostById(id);
        model.addAttribute("posts",post);
        if(user!=null)
            model.addAttribute("userName",user.getName());
//        System.out.println(post);
        return "fullPost";
    }

    @GetMapping("/updatePost/{postId}")
    public String updatePost(@PathVariable("postId")int id,Model model)
    {
        Post post=postService.findPostById(id);
        model.addAttribute("post",post);

        List<Tag> tags=tagService.getAllTags();
        model.addAttribute("tags",tags);
        return "newPost";
    }

    @GetMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable("postId")int postId)
    {
        postService.deletePost(postId);
        return "redirect:/";
    }

    @PostMapping("/saveComment/{postId}")
    public String saveComment(@ModelAttribute("comment")Comment comment,@PathVariable("postId")int postId)
    {
        System.out.println(comment);
        return "";
    }

}
