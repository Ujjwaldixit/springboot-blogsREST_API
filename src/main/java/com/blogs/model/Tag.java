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
public class Tag {

    @Id
    private int id;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
