package com.blogs.service;

import com.blogs.model.Tag;
import com.blogs.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Integer> saveTag(List<Tag> tags) {
        List<Integer> tagIds = new ArrayList<>();
        Tag tagCheck = null;
        HashSet<Tag> uniqueTags = new HashSet<>(tags);
        for (Tag tag : uniqueTags) {
            tagCheck = tagRepository.findByName(tag.getName());
            if (tagCheck == null) {
                Tag newTag = new Tag();
                newTag.setName(tag.getName());
                this.tagRepository.save(newTag);
                tagCheck = tagRepository.findByName(tag.getName());
            }
            tagIds.add(tagCheck.getId());
        }
        return tagIds;
    }

    @Override
    public List<Tag> findTagsByName(List<String> tagsName) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagsName)
            tags.addAll(tagRepository.findTagByNameLike(tagName));
        return tags;
    }
}