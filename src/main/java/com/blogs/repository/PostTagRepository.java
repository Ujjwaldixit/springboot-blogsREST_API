package com.blogs.repository;

import com.blogs.model.PostTagIdentity;
import com.blogs.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostTagIdentity> {
    List<PostTag> findPostTagByPostId(int postId);

    List<PostTag> findPostTagByTagId(int tagId);

    void deletePostTag(PostTag postTag);
}
