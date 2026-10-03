package com.esign.platform.system.menu.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.esign.platform.system.menu.entity.MenuEntity;

public interface MenuRepository
        extends JpaRepository<MenuEntity, UUID>,
                JpaSpecificationExecutor<MenuEntity> {

    boolean existsByMenuNameIgnoreCaseAndDeletedFalse(
            String menuName
    );

    boolean existsByMenuNameIgnoreCaseAndSidebarIdAndDeletedFalse(
            String menuName,
            UUID sidebarId
    );

    boolean existsByMenuNameIgnoreCaseAndSidebarIdIsNullAndDeletedFalse(
            String menuName
    );
    
    List<MenuEntity> findAllBySidebarIdAndDeletedFalseOrderByDisplayOrderAsc(
            UUID sidebarId
    );
}