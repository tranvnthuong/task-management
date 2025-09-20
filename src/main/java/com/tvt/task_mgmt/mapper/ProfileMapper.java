package com.tvt.task_mgmt.mapper;

import com.tvt.task_mgmt.dto.request.ProfileDTO;
import com.tvt.task_mgmt.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "user", ignore = true)
    Profile toUserProfile(ProfileDTO profile);
}
