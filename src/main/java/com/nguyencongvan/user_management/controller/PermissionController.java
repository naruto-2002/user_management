package com.nguyencongvan.user_management.controller;

import com.nguyencongvan.user_management.dto.request.PermissionRequest;
import com.nguyencongvan.user_management.dto.response.ApiResponse;
import com.nguyencongvan.user_management.dto.response.PermissionResponse;
import com.nguyencongvan.user_management.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    @Operation(
            summary = "Create new permission",
            description = "Add a new permission with a specific endpoint and HTTP method. "
                    + "Throws error if the permission already exists."
    )
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(permissionRequest))
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Get list of permissions",
            description = "Returns all permissions configured in the system, each including its endpoint and HTTP method."
    )
    public ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{name}")
    @Operation(
            summary = "Delete a permission by name",
            description = "Remove a permission by its name (ID). Assumes name is unique."
    )
    public ApiResponse<Void> delete(@PathVariable String name) {
        permissionService.delete(name);
        return ApiResponse.<Void>builder().build();
    }
}
