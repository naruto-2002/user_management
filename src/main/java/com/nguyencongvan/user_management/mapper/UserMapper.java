package com.nguyencongvan.user_management.mapper;

import com.nguyencongvan.user_management.dto.request.UserCreationRequest;
import com.nguyencongvan.user_management.dto.request.UserUpdateRequest;
import com.nguyencongvan.user_management.dto.response.UserResponse;
import com.nguyencongvan.user_management.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    //    @Mapping(source = "firstName", target = "lastName")
    //    @Mapping(target = "firstName", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void update(@MappingTarget User user, UserUpdateRequest request);
}
