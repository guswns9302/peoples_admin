package com.peoples.admin.peoples_admin.repository;

import com.peoples.admin.peoples_admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
}
