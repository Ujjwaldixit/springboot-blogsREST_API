package com.blogs.service;

import com.blogs.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    List<Post> getAllPosts();
    int savePost(Post post);
    Post findPostById(int id);
    void deletePost(int id);
    //List<Post> findPostWithPagination(int page,int pageSize);
    Page<Post> findPostWithPaginationAndSorting(int page, int pageSize,String sortingField,String sortingOrder);
    List<Post> findAllLike(String keyword);
}
