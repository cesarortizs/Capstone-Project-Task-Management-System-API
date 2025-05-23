package com.capstone.project.controllers;

import com.capstone.project.exceptions.ResourceNotFoundException;
import com.capstone.project.models.User;
import com.capstone.project.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        logger.info("All users retrieved succesfully");

        return users;
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public long getUserTaskCount(@PathVariable long id)
    {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No user found with id: " + id));
        logger.info("User task count retrieved succesfully");
        return userRepository.getNumberOfTasksPerUser(id);
    }
}
