package com.blogs.service;

import com.blogs.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    Page<Post> getAllPosts(int pageNo, int pageSize, String sortBy);
    void savePost(Post post);
}
