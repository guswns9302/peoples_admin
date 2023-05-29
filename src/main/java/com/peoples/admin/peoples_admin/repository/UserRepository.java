package com.peoples.admin.peoples_admin.repository;

import com.peoples.admin.peoples_admin.domain.Admin;
import com.peoples.admin.peoples_admin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByCreatedAtAfterOrderByCreatedAt(LocalDateTime targetDate);
}
