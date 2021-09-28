package com.blogs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PostTag {

    @Id
    private int postId;
    private int tagId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
