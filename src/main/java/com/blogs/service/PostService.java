package com.blogs.service;

import com.blogs.model.Post;
import com.blogs.model.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface PostService {
    List<Post> getAllPosts();
    int savePost(Post post);
    Post findPostById(int id);
    void deletePost(int id);
    Page<Post> findPostWithPaginationAndSorting(int page, int pageSize,String sortingField,String sortingOrder);
    List<Post> findAllLike(String keyword);
    List<Post> findPostByPostTag(List<PostTag> postTags);
    List<Post> findPostByAuthor(String author);
    List<Post> findPostByPublishedAt(Timestamp publishedAt);
}
