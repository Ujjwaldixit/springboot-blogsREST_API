package com.blogs.service;

import com.blogs.model.Post;
import com.blogs.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<Tag> getAllTags();
    void saveTag(String tag);
}
