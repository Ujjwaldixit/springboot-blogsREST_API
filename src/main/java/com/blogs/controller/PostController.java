package com.blogs.controller;

import com.blogs.model.Post;
import com.blogs.model.PostTag;
import com.blogs.model.Tag;
import com.blogs.repository.PostAndTagRepository;
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
    @Autowired
    private PostAndTagRepository postAndTagRepository;

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
    public String newPost(Model model)
    {
        model.addAttribute("post",new Post());
        List<Tag> tags=tagService.getAllTags();
        model.addAttribute("tags",tags);
        return "/newPost";
    }

    //save post and tags to database
    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("post")Post post,@RequestParam("Tags") String tags, PostTag postTag)
    {
        //save post
        int postId=postService.savePost(post);
        //save tags
        List<Integer> tagIds=tagService.saveTag(tags);

        System.out.println(postId+ "   tag id= "+tagIds);
        //save postId and tagID
        for(int tagId:tagIds)
        {
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            postAndTagRepository.save(postTag);
        }

        return "redirect:/";
    }

    @GetMapping("/fullPost")
    public String displayFullPost()
    {
        return "fullPost";
    }
}
