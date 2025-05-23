package com.capstone.project.controllers;

import com.capstone.project.exceptions.InvalidUserException;
import com.capstone.project.exceptions.ResourceNotFoundException;
import com.capstone.project.models.Task;
import com.capstone.project.repositories.TaskRepository;
import com.capstone.project.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Task> getAllTasks()
    {
        List<Task> tasks = taskRepository.findAll();

        return tasks;
    }

    @GetMapping("/tasks/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Task getTask(@PathVariable int id, Authentication authentication)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No task found with id: " + id));

        if (authentication.getAuthorities()
            .stream()
            .anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN")))
        {
            logger.info("Task retrieved succesfully");
            return existingTask;
        }

        else if (Objects.equals(existingTask.getUser().getId(), userDetails.getId()))
        {
            logger.info("Task retrieved succesfully");
            return existingTask;
        }

        throw new InvalidUserException("You're not authorized to access this resource");
    }

    @PostMapping("/tasks")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void addNewTAsk(@Valid @RequestBody Task newTask)
    {
        taskRepository.save(newTask);
        logger.info("New task created succesfully");
    }

    @PutMapping("/tasks/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void updateTask(@PathVariable int id, @Valid @RequestBody Task updatedTask, Authentication authentication)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No task found with id: " + id));

        if (Objects.equals(existingTask.getUser().getId(), userDetails.getId()))
        {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setPriority(updatedTask.getPriority());

            taskRepository.save(existingTask);

            logger.info("Task updated succesfully");

            return;
        }

        throw new InvalidUserException("You're not authorized to access this resource");
    }

    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteTask(@PathVariable int id, Authentication authentication)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No task found with id: " + id));

        if (authentication.getAuthorities()
            .stream()
            .anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN")))
        {
            taskRepository.deleteById(id);
            logger.info("Task deleted succesfully");
            return;
        }

        if (Objects.equals(existingTask.getUser().getId(), userDetails.getId()))
        {
            taskRepository.deleteById(id);
            logger.info("Task deleted succesfully");
            return;
        }

        throw new InvalidUserException("You're not authorized to access this resource");
    }
}
