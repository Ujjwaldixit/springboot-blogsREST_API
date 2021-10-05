package com.blogs.repository;

import com.blogs.model.PostAndTagIdentity;
import com.blogs.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostAndTagRepository extends JpaRepository<PostTag, PostAndTagIdentity> {
    List<PostTag> getPostTagByPostId(int postId);
}
