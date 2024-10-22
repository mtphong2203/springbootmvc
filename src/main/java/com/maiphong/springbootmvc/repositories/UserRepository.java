package com.maiphong.springbootmvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maiphong.springbootmvc.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    Boolean existsByUsername(String username);
}
