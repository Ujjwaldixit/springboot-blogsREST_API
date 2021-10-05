package com.blogs.controller;
import com.blogs.model.*;
import com.blogs.service.*;
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
    private PostAndTagService postAndTagService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String homePage(@RequestParam(value = "start",defaultValue = "0") int pageNo,
                           @RequestParam(value= "limit",defaultValue = "3") int pageSize,
                           @RequestParam(value = "sortField",defaultValue = "publishedAt")String sortField,
                           @RequestParam(value = "order",defaultValue = "asc")String sortOrder,
                           Model model)
    {
        System.out.println("page ="+pageNo+" "+pageSize);
        Page<Post> _posts= postService.findPostWithPaginationAndSorting(pageNo,pageSize,sortField,sortOrder);
        List<Post> posts=_posts.toList();
        model.addAttribute("posts", posts);
        model.addAttribute("totalPages",_posts.getTotalPages());
        model.addAttribute("limit",pageSize);
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
        int postId=postService.savePost(post);
        //save tags
        List<Integer> tagIds = null;
        if(!tags.equals("")) {
            tagIds = tagService.saveTag(tags);
        }

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
    public String displayFullPost(@AuthenticationPrincipal UserDetailsImpl user,@PathVariable("postId") int postId,Model model)
    {
        Post post=postService.findPostById(postId);
        model.addAttribute("posts",post);
        List<Comment> comments=commentService.getCommentByPostId(postId);
        model.addAttribute("comments",comments);

        if(user!=null)
            model.addAttribute("userName",user.getName());

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
        List<PostTag> postTags=postAndTagService.getPostTagByPostId(postId);
        for(PostTag postTag:postTags)
        {
            postAndTagService.deletePostTag(postTag);
        }
        return "redirect:/";
    }


    @GetMapping("/addComment/{postId}")
    public String addComment(@PathVariable("postId")int postId,Model model,Comment comment)
    {
        model.addAttribute("_comment",comment);
        model.addAttribute("postId",postId);
        return "commentForm";
    }

    @PostMapping("/saveComment")
    public String saveComment(@ModelAttribute("_comment")Comment comment)
    {

        commentService.saveComment(comment);
        return "redirect:/fullPost/"+comment.getPostId();
    }

    @GetMapping("/updateComment/{commentId}")
    public String updateComment(@PathVariable("commentId")int commentId,Model model)
    {
             Comment comment=commentService.getCommentById(commentId);
             model.addAttribute("_comment",comment);
             return "commentForm";
    }

    @GetMapping("/deleteComment/{commentId}/{postId}")
    public String deleteComment(@PathVariable("commentId")int commentId,@PathVariable("postId")int postID)
    {
        System.out.println("inside delete");
        commentService.deleteComment(commentId);
        return "redirect:/fullPost/"+postID;
    }
}
