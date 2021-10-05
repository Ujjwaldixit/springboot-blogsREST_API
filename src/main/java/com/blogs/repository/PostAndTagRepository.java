package com.blogs.repository;

import com.blogs.model.PostAndTagIdentity;
import com.blogs.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostAndTagRepository extends JpaRepository<PostTag, PostAndTagIdentity> {
}
