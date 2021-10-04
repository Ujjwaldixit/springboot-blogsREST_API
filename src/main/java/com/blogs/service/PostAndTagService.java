package com.blogs.service;

import org.springframework.stereotype.Service;

@Service
public interface PostAndTagService {
    void deletePostAndTag(int postId,int tagId);
}
