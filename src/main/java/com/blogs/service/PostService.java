package com.blogs.service;

import com.blogs.model.Post;
import com.blogs.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    public void addPost(Post post)
    {
       postRepository.save(post);
    }
    public List<Post> getAllPosts()
    {
        return postRepository.findAll();
    }
}
