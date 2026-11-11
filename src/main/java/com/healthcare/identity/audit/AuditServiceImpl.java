package com.healthcare.identity.audit;

import com.healthcare.identity.entity.AuditLog;
import com.healthcare.identity.entity.User;
import com.healthcare.identity.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(
            User user,
            String action,
            String entityName,
            Long entityId,
            String description,
            String ipAddress
    ) {

        AuditLog auditLog = AuditLog.builder()
                .user(user)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .description(description)
                .ipAddress(ipAddress)
                .build();

        auditLogRepository.save(auditLog);
    }
}