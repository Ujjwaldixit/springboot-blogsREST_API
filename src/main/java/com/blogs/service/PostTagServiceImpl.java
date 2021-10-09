package com.blogs.service;

import com.blogs.model.PostTag;
import com.blogs.model.Tag;
import com.blogs.repository.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostTagServiceImpl implements PostTagService {
    @Autowired
    private PostTagRepository postTagRepository;

    @Override
    public void savePostTag(PostTag postTag) {
        postTagRepository.save(postTag);
    }

    @Override
    public List<PostTag> findPostTagsByPostId(int postId) {
        return postTagRepository.findPostTagByPostId(postId);
    }

    @Override
    public void deletePostTag(List<PostTag> postAndTags) {
        postTagRepository.deleteAll(postAndTags);
    }

    @Override
    public List<PostTag> findPostTagsByTags(List<Tag> tags) {
        List<PostTag> postTags = new ArrayList<>();
        for (Tag tag : tags) {
            postTags.addAll(postTagRepository.findPostTagByTagId(tag.getId()));
        }
        return postTags;
    }
}
