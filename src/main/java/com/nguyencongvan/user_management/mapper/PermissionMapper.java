package com.nguyencongvan.user_management.mapper;

import com.nguyencongvan.user_management.dto.request.PermissionRequest;
import com.nguyencongvan.user_management.dto.response.PermissionResponse;
import com.nguyencongvan.user_management.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(Permission permission);
}
