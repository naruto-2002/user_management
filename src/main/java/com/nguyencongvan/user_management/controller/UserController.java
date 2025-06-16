package com.nguyencongvan.user_management.controller;

import com.nguyencongvan.user_management.dto.request.UserCreationRequest;
import com.nguyencongvan.user_management.dto.request.UserUpdateRequest;
import com.nguyencongvan.user_management.dto.response.ApiResponse;
import com.nguyencongvan.user_management.dto.response.UserResponse;
import com.nguyencongvan.user_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class UserController {
    UserService userService;

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates a user with default USER role. Throws error if user already exists or role not found."
    )
    public ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest userRequest) {
        log.info("Controller: create user");
        return ApiResponse.<UserResponse>builder()
                .result(userService.create(userRequest))
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieve all users who are not soft-deleted. Requires authentication."
    )
    public ApiResponse<List<UserResponse>> getAll() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority: {}", grantedAuthority));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .build();
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a specific user by their unique ID. Throws error if not found."
    )
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    @Operation(
            summary = "Get authenticated user's info",
            description = "Returns information about the currently authenticated user."
    )
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("{userId}")
    @Operation(
            summary = "Update user information",
            description = "Update user details, including password and roles. Encodes password before saving."
    )
    public ApiResponse<UserResponse> update(
            @PathVariable("userId") String userId,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest) {

        return ApiResponse.<UserResponse>builder()
                .result(userService.update(userId, userUpdateRequest))
                .build();
    }

    @DeleteMapping("{userId}")
    @Operation(
            summary = "Soft delete user",
            description = "Soft deletes a user by setting 'deletedUser' field and nullifying username. Doesn't physically delete from DB."
    )
    public ApiResponse<Void> sofDelete(@PathVariable("userId") String userId) {
        userService.softDelete(userId);
        return ApiResponse.<Void>builder().build();
    }


}
