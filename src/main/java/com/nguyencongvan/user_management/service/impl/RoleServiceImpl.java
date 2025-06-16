package com.nguyencongvan.user_management.service.impl;

import com.nguyencongvan.user_management.dto.request.RoleRequest;
import com.nguyencongvan.user_management.dto.response.RoleResponse;
import com.nguyencongvan.user_management.entity.Permission;
import com.nguyencongvan.user_management.entity.Role;
import com.nguyencongvan.user_management.exception.AppException;
import com.nguyencongvan.user_management.exception.ErrorCode;
import com.nguyencongvan.user_management.mapper.RoleMapper;
import com.nguyencongvan.user_management.repository.PermissionRepository;
import com.nguyencongvan.user_management.repository.RoleRepository;
import com.nguyencongvan.user_management.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);

        List<Permission> permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        try {
            roleRepository.save(role);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRole(String name) {
        Role roleResponse = roleRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
        return roleMapper.toRoleResponse(roleResponse);
    }

    @Override
    public void delete(String name) {
        roleRepository.deleteById(name);
    }
}
