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
import java.sql.Timestamp;


@Service
@Transactional
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;

    @Override
    public Page<Post> getAllPosts(int pageNo,int pageSize,String sortBy,String order) {
        Pageable pageable;
        if(order.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }
        if(order.equals("desc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        else{
            pageable=PageRequest.of(pageNo,pageSize);
        }

        Page<Post> pageResult=postRepository.findAll(pageable);
        return pageResult;
    }

    public int savePost(Post post)
    {
        //setting excerpt
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
}
