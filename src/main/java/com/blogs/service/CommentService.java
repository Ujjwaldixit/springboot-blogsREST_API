package com.blogs.service;

import com.blogs.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    public void saveComment(Comment comment);
    public void deleteComment(int commentId);
    public List<Comment> getCommentByPostId(int postId);
    public Comment getCommentById(int commentId);
}
