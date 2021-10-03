package com.blogs.service;

import com.blogs.model.Post;
import com.blogs.model.Tag;
import com.blogs.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService{

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public void saveTag(Tag tag) {
        this.tagRepository.save(tag);
    }
}

