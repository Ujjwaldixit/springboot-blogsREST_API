package com.blogs.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PostAndTagIdentity implements Serializable {

    private int postId;
    private int tagId;
}
