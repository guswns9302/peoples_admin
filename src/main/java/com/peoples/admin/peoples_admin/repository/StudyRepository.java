package com.peoples.admin.peoples_admin.repository;

import com.peoples.admin.peoples_admin.domain.Study;
import com.peoples.admin.peoples_admin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StudyRepository extends JpaRepository<Study, String> {
    List<Study> findAllByCreatedAtAfterOrderByCreatedAt(LocalDateTime targetDate);
}
