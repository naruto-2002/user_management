package com.nguyencongvan.user_management.service.impl;

import com.nguyencongvan.user_management.dto.request.PermissionRequest;
import com.nguyencongvan.user_management.dto.response.PermissionResponse;
import com.nguyencongvan.user_management.entity.Permission;
import com.nguyencongvan.user_management.exception.AppException;
import com.nguyencongvan.user_management.exception.ErrorCode;
import com.nguyencongvan.user_management.mapper.PermissionMapper;
import com.nguyencongvan.user_management.repository.PermissionRepository;
import com.nguyencongvan.user_management.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class PermissionServiceImpl implements PermissionService {
    AntPathMatcher pathMatcher = new AntPathMatcher();
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);

        try {
            permissionRepository.save(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }

        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {

        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String name) {
        permissionRepository.deleteById(name);
    }

    @Override
    public boolean checkPermission(Authentication auth, String endpoint, String method) {

        if (auth == null || !(auth instanceof AbstractAuthenticationToken) || !auth.isAuthenticated()) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities == null) {
            return false;
        }

        return authorities.stream()
                .filter(Objects::nonNull)
                .map(GrantedAuthority::getAuthority)
                .filter(authorityStr -> authorityStr != null && !authorityStr.contains("ROLE_"))
                .anyMatch(authorityStr -> {
                    Permission permission = permissionRepository.findById(authorityStr).orElse(null);

                    String permittedEndpoint = permission.getEndpoint();
                    String permittedMethod = permission.getMethod();

                    return pathMatcher.match(permittedEndpoint, endpoint)
                            && method.equalsIgnoreCase(permittedMethod);
                });
    }
}
