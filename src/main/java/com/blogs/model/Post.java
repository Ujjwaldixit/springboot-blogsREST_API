package com.blogs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    private int id;
    private String title;
    private String excerpt;
    private String content;
    private String author;
    private Timestamp publishedAt;
    private boolean isPublished;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
