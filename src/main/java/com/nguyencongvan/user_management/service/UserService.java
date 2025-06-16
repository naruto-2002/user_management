package com.nguyencongvan.user_management.service;

import com.nguyencongvan.user_management.dto.request.UserCreationRequest;
import com.nguyencongvan.user_management.dto.request.UserUpdateRequest;
import com.nguyencongvan.user_management.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreationRequest request);

    List<UserResponse> getAll();

    UserResponse getUser(String id);

    UserResponse update(String id, UserUpdateRequest request);

    void delete(String id);

    UserResponse getMyInfo();

    void softDelete(String id);
}
