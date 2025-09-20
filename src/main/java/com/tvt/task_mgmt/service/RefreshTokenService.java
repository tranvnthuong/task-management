package com.tvt.task_mgmt.service;

import com.tvt.task_mgmt.dto.return_method.RefreshTokenReturn;
import com.tvt.task_mgmt.entity.User;
import jakarta.transaction.Transactional;

public interface RefreshTokenService {
    RefreshTokenReturn generateRefreshToken(User user, String ip, String userAgent);

    RefreshTokenReturn generateRefreshToken(String token, String ip, String userAgent);

    @Transactional
    void revokeToken(String token);
}
