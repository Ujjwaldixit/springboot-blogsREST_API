package com.blogs.service;

import com.blogs.model.PostTag;
import com.blogs.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostTagService {
    void savePostTag(PostTag postTag);

    List<PostTag> findPostTagsByPostId(int postId);

    void deletePostTags(List<PostTag> postAndTags);

    List<PostTag> findPostTagsByTags(List<Tag> tags);

    void savePostTags(int postId,List<Tag> Tags);
}
