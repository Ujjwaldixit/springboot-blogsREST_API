package com.blogs.service;

import com.blogs.repository.PostAndTagRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PostAndTagServiceImpl implements PostAndTagService{
    @Autowired
    private PostAndTagRepository postAndTagRepository;

    @Override
    public void deletePostAndTag(int postId, int tagId) {

    }
}
