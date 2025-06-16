package com.nguyencongvan.user_management.service;

import com.nguyencongvan.user_management.dto.request.PermissionRequest;
import com.nguyencongvan.user_management.dto.response.PermissionResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest permissionRequest);

    List<PermissionResponse> getAll();

    void delete(String name);

    boolean checkPermission(Authentication auth, String endpoint, String method);

}
