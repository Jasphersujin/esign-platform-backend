package com.esign.platform.common.audit.repository;

import com.esign.platform.common.audit.entity.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AuditTrailRepository extends JpaRepository<AuditTrail, UUID>,
        JpaSpecificationExecutor<AuditTrail> {
}