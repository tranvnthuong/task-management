package com.tvt.task_mgmt.service.impl;

import com.tvt.task_mgmt.dto.request.ChangeEmailRequest;
import com.tvt.task_mgmt.dto.request.ChangePasswordRequest;
import com.tvt.task_mgmt.dto.request.UserRequest;
import com.tvt.task_mgmt.dto.request.ProfileDTO;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.exception.ErrorCode;
import com.tvt.task_mgmt.dto.response.UserResponse;
import com.tvt.task_mgmt.entity.Role;
import com.tvt.task_mgmt.entity.User;
import com.tvt.task_mgmt.exception.AppException;
import com.tvt.task_mgmt.mapper.ProfileMapper;
import com.tvt.task_mgmt.mapper.UserMapper;
import com.tvt.task_mgmt.repository.RoleRepository;
import com.tvt.task_mgmt.repository.UserRepository;
import com.tvt.task_mgmt.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public MyApiResponse<UserResponse> getSelfInfo() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXIST));

        UserResponse response = userMapper.toUserResponse(user);
        return MyApiResponse.of(ErrorCode.SUCCESS, response);
    }

    @Override
    public MyApiResponse<UserResponse> createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_ALREADY_EXIST);

        User user = userMapper.toUser(request);
        user.getProfile().setUser(user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Optional<Role> userRole = roleRepository.findById("USER");
        if (userRole.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole.get());
            user.setRoles(roles);
        }
        userRepository.save(user);

        UserResponse response = userMapper.toUserResponse(user);
        return MyApiResponse.of(ErrorCode.USER_CREATED, response);
    }

    @Override
    public MyApiResponse<UserResponse> userProfileUpdate(String userId, ProfileDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_DOES_NOT_EXIST));


        user.setProfile(profileMapper.toUserProfile(request));
        userRepository.save(user);

        UserResponse response = userMapper.toUserResponse(user);

        return MyApiResponse.of(ErrorCode.USER_UPDATED, response);
    }

    @Override
    public MyApiResponse<Object> deleteUser(String userId) {
        userRepository.deleteById(userId);
        return MyApiResponse.of(ErrorCode.USER_DELETED);
    }

    @Override
    public MyApiResponse<List<UserResponse>> getAllUserInfo() {
        List<User> users = userRepository.findAll();
        return MyApiResponse.of(ErrorCode.SUCCESS, userMapper.toUserResponseList(users));
    }

    @Override
    public MyApiResponse<UserResponse> getUserById(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new AppException(ErrorCode.USER_DOES_NOT_EXIST));
        return MyApiResponse.of(ErrorCode.SUCCESS, userMapper.toUserResponse(user));
    }

    @Override
    public MyApiResponse<Object> changePassword(String userId, ChangePasswordRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXIST));

        boolean matchPassword = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
        if (!matchPassword)
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        user.setPassword(passwordEncoder.encode(request.getOldPassword()));

        return MyApiResponse.of(ErrorCode.SUCCESS);
    }

    @Override
    public MyApiResponse<Object> changeEmail(String userId, ChangeEmailRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXIST));

        boolean matchPassword = passwordEncoder.matches(request.getConfirmPassword(), user.getPassword());
        if (!matchPassword)
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        user.setEmail(request.getEmail());

        return MyApiResponse.of(ErrorCode.SUCCESS);
    }
}
