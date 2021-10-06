package com.blogs.service;

import com.blogs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean register(User user);
}
