package com.nguyencongvan.user_management.repository;

import com.nguyencongvan.user_management.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {}
