package com.blogs.service;

import com.blogs.model.Post;
import com.blogs.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;

    @Override
    public Page<Post> getAllPosts(int pageNo,int pageSize,String sortBy) {
        Pageable pageable=PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<Post> pageResult=postRepository.findAll(pageable);
        return pageResult;
    }

    public void savePost(Post post)
    {
        this.postRepository.save(post);
    }

//    @Override
//    public Page<Post> findPaginated(int pageNo, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//        return this.postRepository.findAll(pageable);
//    }
}
