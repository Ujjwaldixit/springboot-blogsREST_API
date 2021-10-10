package com.blogs.controller;

import com.blogs.model.*;
import com.blogs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
                                               @RequestParam(value = "publishedAt", required = false) List<String> publishedAt) {

        try {
            Page<Post> sortedAndPaginatedPosts = postService.findPostsWithPaginationAndSorting(
                    pageNo, pageSize, sortField, sortOrder);

            List<Post> posts = sortedAndPaginatedPosts.toList();

            if (searchKeyword != null) {
                sortedAndPaginatedPosts = null;
                posts = postService.findPostsByKeyword(searchKeyword);

                List<Tag> tags = tagService.findTagsByName(List.of(searchKeyword));

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
    public ResponseEntity<PostAndComments> displayFullPost(@AuthenticationPrincipal UserDetailsImpl user,
                                                           @PathVariable("postId") int postId) {
        try {
            Post post = postService.findPostById(postId);

            List<Comment> comments = new ArrayList<>();

            comments = commentService.findCommentsByPostId(postId);

            PostAndComments postAndComments = new PostAndComments(post, comments);

            return new ResponseEntity<>(postAndComments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updatePost/{postId}")
    public ResponseEntity<?> updatePost(@AuthenticationPrincipal UserDetailsImpl user,
                                        @PathVariable("postId") int id,
                                        @RequestBody PostAndTags postAndTags) {
        try {
            Post post = postService.findPostById(id);

            if (!user.getName().equals(post.getAuthor()))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            if (postAndTags.getPost().getTitle() != null)
                post.setTitle(postAndTags.getPost().getTitle());

            if (postAndTags.getPost().getContent() != null)
                post.setContent(postAndTags.getPost().getContent());

            List<PostTag> postTags = postTagService.findPostTagsByPostId(id);

            tagService.saveTag(postAndTags.getTags());
            postTagService.deletePostTags(postTags);
            postTagService.savePostTags(postAndTags.getPost().getId(),postAndTags.getTags());
            postService.savePost(post);

            return new ResponseEntity<>(postAndTags, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletePost/{postId}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal UserDetailsImpl user,
                                        @PathVariable("postId") int postId) {
        try {
            Post post=postService.findPostById(postId);

            if (!user.getName().equals(post.getAuthor()))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            postService.deletePost(postId);
            postTagService.deletePostTags(postTagService.findPostTagsByPostId(postId));
            commentService.deleteCommentByPostId(postId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addComment/{postId}")
    public ResponseEntity<?> saveComment(@RequestBody Comment comment,
                                         @PathVariable("postId") int postId) {
        try {
            comment.setPostId(postId);

            commentService.saveComment(comment);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateComment/{commentId}/{postId}")
    public ResponseEntity<Comment> updateComment(@AuthenticationPrincipal UserDetailsImpl user,
                                                 @PathVariable("commentId") int commentId,
                                                 @PathVariable("postId") int postId,
                                                 @RequestBody Comment comment) {
        try {
            Post post=postService.findPostById(postId);

            if (!user.getName().equals(post.getAuthor()))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            comment.setPostId(postId);
            comment.setId(commentService.findCommentById(commentId).getId());
            comment.setCreatedAt(commentService.findCommentById(commentId).getCreatedAt());

            commentService.saveComment(comment);

            return new ResponseEntity<>(comment, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteComment/{commentId}/{postId}")
    public ResponseEntity<Comment> deleteComment(@AuthenticationPrincipal UserDetailsImpl user,
                                                 @PathVariable("commentId") int commentId,
                                                 @PathVariable("postId") int postId) {
        try {
            Post post=postService.findPostById(postId);

            if (!user.getName().equals(post.getAuthor()))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            commentService.deleteComment(commentId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}