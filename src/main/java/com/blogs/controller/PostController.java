package com.blogs.controller;

import com.blogs.model.*;
import com.blogs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;
    @Autowired
    private PostTagService postTagService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String homePage(@RequestParam(value = "start", defaultValue = "0") int pageNo,
                           @RequestParam(value = "limit", defaultValue = "10") int pageSize,
                           @RequestParam(value = "sortField", defaultValue = "publishedAt") String sortField,
                           @RequestParam(value = "order", defaultValue = "asc") String sortOrder,
                           @RequestParam(value = "search", required = false) String searchKeyword,
                           @RequestParam(value = "author", required = false) List<String> authors,
                           @RequestParam(value = "tag", required = false) List<String> tagsName,
                           @RequestParam(value = "publishedAt", required = false) List<Timestamp> publishedAt,
                           Model model) {

        Page<Post> sortedAndPaginatedPosts = postService.findPostsWithPaginationAndSorting(pageNo, pageSize, sortField, sortOrder);
        List<Post> posts = sortedAndPaginatedPosts.toList();

        if (searchKeyword != null) {
            sortedAndPaginatedPosts = null;
            posts = postService.findPostsByKeyword(searchKeyword);
            if (posts.size() == 0) {
                List<Tag> tags = tagService.findTagsByName(Arrays.asList(searchKeyword));
                if (tags != null) {
                    List<PostTag> postTags = postTagService.findPostTagsByTags(tags);
                    posts = postService.findPostsByPostTag(postTags);
                }
            }
        }

        if (authors != null || tagsName != null || publishedAt != null) {
            sortedAndPaginatedPosts = null;
            posts = new ArrayList<>();
            if (authors != null) {
                posts.addAll(postService.findPostsByAuthor(authors));
            }
            if (publishedAt != null) {
                posts.addAll(postService.findPostsByPublishedAt(publishedAt));
            }
            if (tagsName != null) {
                List<Tag> tags = tagService.findTagsByName(tagsName);
                if (tags != null) {
                    List<PostTag> postTags = postTagService.findPostTagsByTags(tags);
                    posts.addAll(postService.findPostsByPostTag(postTags));

                }
            }
        }

        if (sortedAndPaginatedPosts != null)
            model.addAttribute("totalPages", sortedAndPaginatedPosts.getTotalPages());

        model.addAttribute("posts", posts);
        model.addAttribute("start", pageNo);
        model.addAttribute("limit", pageSize);
        model.addAttribute("keyword", searchKeyword);
        return "index";
    }

    @GetMapping("/showNewPostForm")
    public String newPost(@AuthenticationPrincipal UserDetailsImpl user, Model model, Post post) {
        post.setAuthor(user.getName());
        model.addAttribute("post", post);
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);
        return "/newPost";
    }

    @PostMapping("/savePost")
    public String savePost(@ModelAttribute("post") Post post, @RequestParam("Tags") String tags, PostTag postTag) {
        post = postService.savePost(post);
        int postId = post.getId();
        List<Integer> tagIds = new ArrayList<>();
        if (tags.length() > 0) {
            tagIds = tagService.saveTag(tags);
        }

        postTag.setPostId(postId);
        if (tagIds.size() > 0) {
            for (int tagId : tagIds) {
                postTag.setTagId(tagId);
                postTagService.savePostTag(postTag);
            }
        } else {
            postTag.setTagId(0);
            postTagService.savePostTag(postTag);
        }
        return "redirect:/";
    }

    @GetMapping("/fullPost/{postId}")
    public String displayFullPost(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("postId") int postId, Model model) {
        Post post = postService.findPostById(postId);
        model.addAttribute("post", post);
        List<Comment> comments = commentService.findCommentsByPostId(postId);
        model.addAttribute("comments", comments);

        if (user != null)
            model.addAttribute("userName", user.getName());

        return "fullPost";
    }

    @GetMapping("/updatePost/{postId}")
    public String updatePost(@PathVariable("postId") int id, Model model) {
        Post post = postService.findPostById(id);
        model.addAttribute("post", post);

        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);
        return "newPost";
    }

    @GetMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable("postId") int postId) {
        postService.deletePost(postId);
        List<PostTag> postTags = postTagService.findPostTagsByPostId(postId);
        for (PostTag postTag : postTags) {
            postTagService.deletePostTag(postTag);
        }
        return "redirect:/";
    }

    @GetMapping("/addComment/{postId}")
    public String addComment(@PathVariable("postId") int postId, Model model, Comment comment) {
        model.addAttribute("_comment", comment);
        model.addAttribute("postId", postId);
        return "commentForm";
    }

    @PostMapping("/saveComment")
    public String saveComment(@ModelAttribute("_comment") Comment comment) {
        commentService.saveComment(comment);
        return "redirect:/fullPost/" + comment.getPostId();
    }

    @GetMapping("/updateComment/{commentId}")
    public String updateComment(@PathVariable("commentId") int commentId, Model model) {
        Comment comment = commentService.findCommentById(commentId);
        model.addAttribute("_comment", comment);
        return "commentForm";
    }

    @GetMapping("/deleteComment/{commentId}/{postId}")
    public String deleteComment(@PathVariable("commentId") int commentId, @PathVariable("postId") int postID) {
        commentService.deleteComment(commentId);
        return "redirect:/fullPost/" + postID;
    }
}
