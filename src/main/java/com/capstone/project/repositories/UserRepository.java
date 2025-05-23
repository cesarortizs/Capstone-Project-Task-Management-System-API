package com.capstone.project.repositories;

import com.capstone.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT COUNT (*) FROM Task WHERE user.id = :id_user")
    Long getNumberOfTasksPerUser(@Param("id_user") long user_id);
}
