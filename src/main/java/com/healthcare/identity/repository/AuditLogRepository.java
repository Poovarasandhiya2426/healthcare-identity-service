package com.healthcare.identity.repository;

import com.healthcare.identity.entity.AuditLog;
import com.healthcare.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUser(User user);

    List<AuditLog> findByAction(String action);

    List<AuditLog> findByUserOrderByCreatedAtDesc(User user);

}