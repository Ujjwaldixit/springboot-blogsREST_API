package com.blogs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostAndComments {

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Post post;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Comment> comments;
}
