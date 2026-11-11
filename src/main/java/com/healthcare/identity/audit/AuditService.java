package com.healthcare.identity.audit;

import com.healthcare.identity.entity.User;

public interface AuditService {

    void log(
            User user,
            String action,
            String entityName,
            Long entityId,
            String description,
            String ipAddress
    );
}