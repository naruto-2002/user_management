package com.nguyencongvan.user_management.service.impl;

import com.nguyencongvan.user_management.dto.request.UserCreationRequest;
import com.nguyencongvan.user_management.dto.request.UserUpdateRequest;
import com.nguyencongvan.user_management.dto.response.UserResponse;
import com.nguyencongvan.user_management.entity.Role;
import com.nguyencongvan.user_management.entity.User;
import com.nguyencongvan.user_management.enums.RoleUser;
import com.nguyencongvan.user_management.exception.AppException;
import com.nguyencongvan.user_management.exception.ErrorCode;
import com.nguyencongvan.user_management.mapper.UserMapper;
import com.nguyencongvan.user_management.repository.RoleRepository;
import com.nguyencongvan.user_management.repository.UserRepository;
import com.nguyencongvan.user_management.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class UserServiceImpl implements UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;

    @Override
    public UserResponse create(UserCreationRequest userCreationRequest) {
        log.info("Service: create user");

        User user = userMapper.toUser(userCreationRequest);
        PasswordEncoder passworEncoder = new BCryptPasswordEncoder();
        user.setPassword(passworEncoder.encode(userCreationRequest.getPassword()));

        if(!roleRepository.existsById(RoleUser.USER.name())) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        List<Role> roles = roleRepository.findById(RoleUser.USER.name())
                .map(List::of)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setRoles(new HashSet<>(roles));

        try {
            user = userRepository.save(user);
        }catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        log.info("In method getAll");

        return userRepository.findAll().stream()
                .filter(user -> user.getDeletedUser() == null)
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }


    @Override
    public UserResponse getUser(String id) {
        log.info("In method getUser");

        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public UserResponse update(String id, UserUpdateRequest userUpdateRequest) {
        log.info("In method update");

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.update(user, userUpdateRequest);

        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));

        List<Role> roles = roleRepository.findAllById(userUpdateRequest.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXITED));

        return userMapper.toUserResponse(user);
    }

    @Override
    public void softDelete(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXITED));

        user.setDeletedUser(user.getUsername());
        user.setUsername(null);

        userRepository.save(user);
    }
}
