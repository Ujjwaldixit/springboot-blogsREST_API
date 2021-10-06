package com.blogs.repository;

import com.blogs.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByAuthorLike(String keyword);

    List<Post> findByTitleLike(String keyword);

    List<Post> findByContentLike(String keyword);

    List<Post> findByExcerptLike(String keyword);

    List<Post> findByAuthor(String author);

    List<Post> findByPublishedAt(Timestamp publishedAt);

    //int findIdByPost(Post post);
}
