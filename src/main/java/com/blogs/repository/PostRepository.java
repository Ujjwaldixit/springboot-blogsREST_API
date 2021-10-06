package com.blogs.repository;

import com.blogs.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Slice<Post> findByAuthorLike(String keyword, Pageable pageable);

    Slice<Post> findByTitleLike(String keyword, Pageable pageable);

    Slice<Post> findByContentLike(String keyword, Pageable pageable);

    Slice<Post> findByExcerptLike(String keyword, Pageable pageable);

    Slice<Post> findByAuthor(String author,Pageable pageable);

    Slice<Post> findByPublishedAt(Timestamp publishedAt,Pageable pageable);
}
