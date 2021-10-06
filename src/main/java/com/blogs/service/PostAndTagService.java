package com.blogs.service;

import com.blogs.model.PostAndTagIdentity;
import com.blogs.model.PostTag;
import com.blogs.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostAndTagService {
    void addPostTag(PostTag postTag);

    List<PostTag> getPostTagByPostId(int postId);

    void deletePostTag(PostTag postAndTag);

    List<PostTag> getPostTagByTags(List<Tag> tag);
}
