package com.blogs.service;

import com.blogs.model.User;
import com.blogs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean register(User user) {
        if (user != null && !userRepository.existsById(user.getUserId())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);
            userRepository.save(user);

            return true;
        } else {
            return false;
        }
    }
}
