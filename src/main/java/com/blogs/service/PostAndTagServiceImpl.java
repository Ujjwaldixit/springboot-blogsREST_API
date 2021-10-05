package com.blogs.service;

import com.blogs.model.PostAndTagIdentity;
import com.blogs.model.PostTag;
import com.blogs.repository.PostAndTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PostAndTagServiceImpl implements PostAndTagService{
    @Autowired
    private PostAndTagRepository postAndTagRepository;

    @Override
    public void addPostTag(PostTag postTag) {
        postAndTagRepository.save(postTag);
    }

    @Override
    public List<PostTag> getPostTagByPostId(int postId) {
        return postAndTagRepository.getPostTagByPostId(postId);
    }

    @Override
    public void deletePostTag(PostTag postTag) {
        postAndTagRepository.delete(postTag);
    }


}
