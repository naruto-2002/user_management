package com.nguyencongvan.user_management.service;

import com.nguyencongvan.user_management.dto.request.RoleRequest;
import com.nguyencongvan.user_management.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest roleRequest);

    List<RoleResponse> getAll();

    RoleResponse getRole(String name);

    void delete(String name);
}
