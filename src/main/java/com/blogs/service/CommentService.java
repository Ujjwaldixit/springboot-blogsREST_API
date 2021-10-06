package com.blogs.service;

import com.blogs.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    void saveComment(Comment comment);

    void deleteComment(int commentId);

    List<Comment> getCommentByPostId(int postId);

    Comment getCommentById(int commentId);
}
