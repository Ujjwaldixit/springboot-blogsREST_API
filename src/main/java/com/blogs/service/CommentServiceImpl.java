package com.blogs.service;

import com.blogs.model.Comment;
import com.blogs.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements  CommentService{

    @Autowired
    private CommentRepository commentRepository;
    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(int commentId) {

    }

    @Override
    public List<Comment> getCommentByPostId(int postId) {
        List<Comment> comments=commentRepository.getCommentByPostId(postId);
        System.out.println(comments);
        return comments;
    }

    @Override
    public Comment getCommentById(int commentId) {
        return commentRepository.getOne(commentId);
    }
}
