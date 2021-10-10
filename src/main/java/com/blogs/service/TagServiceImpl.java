package com.blogs.service;

import com.blogs.model.Tag;
import com.blogs.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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

        HashSet<Tag> uniqueTags = new HashSet<>(tags);

        for (Tag tag : uniqueTags) {
            tag = tagRepository.findByName(tag.getName());
            if (tag == null) {
                Tag newTag = new Tag();
                newTag.setName(tag.getName());
                this.tagRepository.save(newTag);
                tag = tagRepository.findByName(tag.getName());
            }
            tagIds.add(tag.getId());
        }

        return tagIds;
    }

    @Override
    public List<Tag> findTagsByName(List<String> tagsNames) {
        List<Tag> tags = new ArrayList<>();

        for (String tagName : tagsNames)
            tags.addAll(tagRepository.findTagByNameLike(tagName));

        return tags;
    }

    @Override
    public List<Tag> findTagsByIds(List<Integer> tagsIds) {
        List<Tag> tags = new ArrayList<>();

        for (int tagId : tagsIds)
            tags.add(tagRepository.getOne(tagId));

        return tags;
    }
}