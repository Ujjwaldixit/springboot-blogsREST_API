package com.blogs.service;

import com.blogs.model.Post;
import com.blogs.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


@Service
@Transactional
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts=postRepository.findAll();
        return posts;
    }

    public int savePost(Post post)
    {
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        if(post.getContent().length()>=100)
        {
            post.setExcerpt(post.getContent().substring(0,90));
        }
        else{
            post.setExcerpt(post.getContent());
        }
        this.postRepository.save(post);
        return post.getId();
    }

    @Override
    public Post findPostById(int id) {
       Post post= postRepository.getOne(id);
       return post;
    }

    @Override
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public Page<Post> findPostWithPaginationAndSorting(int page, int pageSize,String sortingField,String sortingOrder) {
        page=page/pageSize;
        if(sortingOrder.equals("asc")||sortingOrder.equals("ASC")||sortingOrder.equals("Asc"))
            return postRepository.findAll(PageRequest.of(page,pageSize).withSort(Sort.by(Sort.Direction.ASC,sortingField)));
        return postRepository.findAll(PageRequest.of(page,pageSize).withSort(Sort.by(Sort.Direction.DESC,sortingField)));
    }

    @Override
    public List<Post> findAllLike(String keyword) {
        List<Post> post= postRepository.findByExcerptLike(keyword);
        post.addAll(postRepository.findByAuthorLike(keyword));
        post.addAll(postRepository.findByTitleLike(keyword));
        post.addAll(postRepository.findByContentLike(keyword));
        HashSet<Post> posts=new HashSet<>(post);
        return new ArrayList<>(posts);
    }


}
