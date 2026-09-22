package com.esign.platform.system.sidebar.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.esign.platform.system.sidebar.entity.SidebarEntity;

@Repository
public interface SidebarRepository extends JpaRepository<SidebarEntity, UUID>,
                JpaSpecificationExecutor<SidebarEntity> {

    boolean existsByDisplayNameIgnoreCaseAndDeletedFalse(
            String displayName
    );
}