package com.tvt.task_mgmt.mapper;

import com.tvt.task_mgmt.dto.request.UserRequest;
import com.tvt.task_mgmt.dto.response.UserResponse;
import com.tvt.task_mgmt.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request);
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);
}
