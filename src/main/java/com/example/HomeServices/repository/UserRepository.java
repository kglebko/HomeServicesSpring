package com.example.HomeServices.repository;

import com.example.HomeServices.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
