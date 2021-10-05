package com.blogs.service;

import com.blogs.model.Comment;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    public void addComment(Comment comment);

}
