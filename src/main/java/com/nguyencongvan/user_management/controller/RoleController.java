package com.nguyencongvan.user_management.controller;

import com.nguyencongvan.user_management.dto.request.RoleRequest;
import com.nguyencongvan.user_management.dto.response.ApiResponse;
import com.nguyencongvan.user_management.dto.response.RoleResponse;
import com.nguyencongvan.user_management.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class RoleController {
    RoleService roleService;

    @PostMapping
    @Operation(
            summary = "Create new role",
            description = "Create a new role with a set of permissions. Throws error if the role already exists."
    )
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(roleRequest))
                .build();
    }

    @GetMapping("/{name}")
    @Operation(
            summary = "Get role by name",
            description = "Retrieve a role by its name, including associated permissions."
    )
    public ApiResponse<RoleResponse> getRole(@PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.getRole(name))
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Get all roles",
            description = "Retrieve a list of all roles defined in the system, with their permissions."
    )
    public ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{name}")
    @Operation(
            summary = "Delete role by name",
            description = "Remove a role from the system using its name."
    )
    public ApiResponse<Void> delete(@PathVariable String name) {
        roleService.delete(name);
        return ApiResponse.<Void>builder().build();
    }

}
