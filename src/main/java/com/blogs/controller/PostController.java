package com.blogs.controller;

import com.blogs.model.*;
import com.blogs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
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
    public ResponseEntity<List<Post>> homePage(@RequestParam(value = "start", defaultValue = "0") int pageNo,
                                               @RequestParam(value = "limit", defaultValue = "3") int pageSize,
                                               @RequestParam(value = "sortField", defaultValue = "publishedAt") String sortField,
                                               @RequestParam(value = "order", defaultValue = "asc") String sortOrder,
                                               @RequestParam(value = "search", required = false) String searchKeyword,
                                               @RequestParam(value = "author", required = false) List<String> authors,
                                               @RequestParam(value = "tag", required = false) List<String> tagsName,
                                               @RequestParam(value = "publishedAt", required = false) List<String> publishedAt,
                                               Model model) {

        try {
            Page<Post> sortedAndPaginatedPosts = postService.findPostsWithPaginationAndSorting(pageNo, pageSize, sortField, sortOrder);
            List<Post> posts = sortedAndPaginatedPosts.toList();

            if (searchKeyword != null) {
                sortedAndPaginatedPosts = null;
                posts = postService.findPostsByKeyword(searchKeyword);
                List<Tag> tags = tagService.findTagsByName(Arrays.asList(searchKeyword));
                if (tags != null) {
                    List<PostTag> postTags = postTagService.findPostTagsByTags(tags);
                    posts = postService.findPostsByPostTag(postTags);
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

            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/newPost")
    public ResponseEntity<Post> savePost(@AuthenticationPrincipal UserDetailsImpl user,
                                         @RequestBody PostAndTags postAndTags,
                                         PostTag postTag) {
        Post post = postAndTags.getPost();

        List<Tag> tags = postAndTags.getTags();

        try {
            post.setAuthor(user.getName());
            post = postService.savePost(post);

            int postId = post.getId();

            List<Integer> tagIds = new ArrayList<>();

            if (tags.size() > 0) {
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

            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fullPost/{postId}")
    public ResponseEntity<PostAndComments> displayFullPost(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("postId") int postId) {
        try {
            Post post = postService.findPostById(postId);

            List<Comment> comments =new ArrayList<>();

            comments= commentService.findCommentsByPostId(postId);

            PostAndComments postAndComments=new PostAndComments(post,comments);

            return new ResponseEntity<>(postAndComments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/updatePost/{postId}")
    public String updatePost(@PathVariable("postId") int id, Model model) {
        try {
            Post post = postService.findPostById(id);

            List<Tag> tags = tagService.getAllTags();
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @DeleteMapping("/deletePost/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") int postId) {
        try {
            postService.deletePost(postId);
            List<PostTag> postTags = postTagService.findPostTagsByPostId(postId);
            for (PostTag postTag : postTags) {
                postTagService.deletePostTag(postTag);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addComment/{postId}")
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

    @PutMapping("/updateComment/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("commentId") int commentId, Model model) {
        try {
            Comment comment = commentService.findCommentById(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteComment/{commentId}/{postId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable("commentId") int commentId, @PathVariable("postId") int postID) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
