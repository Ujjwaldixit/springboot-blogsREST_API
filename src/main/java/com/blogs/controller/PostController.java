package com.blogs.controller;

import com.blogs.model.Post;
import com.blogs.model.PostTag;
import com.blogs.model.Tag;
import com.blogs.repository.PostAndTagRepository;
import com.blogs.service.PostService;
import com.blogs.service.TagService;
import com.blogs.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String newPost(@AuthenticationPrincipal UserDetailsImpl user, Model model)
    {

        Post post=new Post();
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

    @GetMapping("/fullPost/{postId}")
    public String displayFullPost(@AuthenticationPrincipal UserDetailsImpl user,@PathVariable("postId") int id,Model model)
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
}
