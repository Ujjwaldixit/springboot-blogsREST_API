package com.blogs.service;

import com.blogs.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Page<Post> getAllPosts(int pageNo, int pageSize, String sortBy,String order);
    int savePost(Post post);
    Post findPostById(int id);
    void deletePost(int id);
}
