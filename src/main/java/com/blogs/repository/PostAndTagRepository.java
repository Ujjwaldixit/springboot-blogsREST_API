package com.blogs.repository;

import com.blogs.model.PostAndTagIdentity;
import com.blogs.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostAndTagRepository extends JpaRepository<PostTag, PostAndTagIdentity> {
}
