package com.blogs.service;

import com.blogs.model.PostAndTagIdentity;
import com.blogs.model.PostTag;
import com.blogs.repository.PostAndTagRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PostAndTagServiceImpl implements PostAndTagService{
    @Autowired
    private PostAndTagRepository postAndTagRepository;

    @Override
    public void addPostTag(PostTag postTag) {
        postAndTagRepository.save(postTag);
    }

    @Override
    public void deletePostAndTag(int postId) {
        postAndTagRepository.deletePostTag(postId);
    }
}
