package com.nguyencongvan.user_management.mapper;

import com.nguyencongvan.user_management.dto.request.RoleRequest;
import com.nguyencongvan.user_management.dto.response.RoleResponse;
import com.nguyencongvan.user_management.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
