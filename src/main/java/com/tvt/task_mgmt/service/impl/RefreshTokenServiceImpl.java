package com.tvt.task_mgmt.service.impl;

import com.tvt.task_mgmt.service.AuthService;
import com.tvt.task_mgmt.exception.ErrorCode;
import com.tvt.task_mgmt.dto.return_method.RefreshTokenReturn;
import com.tvt.task_mgmt.exception.AppException;
import com.tvt.task_mgmt.entity.RefreshToken;
import com.tvt.task_mgmt.entity.User;
import com.tvt.task_mgmt.repository.RefreshTokenRepository;
import com.tvt.task_mgmt.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    AuthService authService;
    RefreshTokenRepository refreshTokenRepository;

    @NonFinal
    @Value("${jwt.access-token-expiry}")
    protected int ACCESS_TOKEN_EXPIRY;

    @NonFinal
    @Value("${jwt.refresh-token-expiry}")
    protected int REFRESH_TOKEN_EXPIRY;

    @Override
    public RefreshTokenReturn generateRefreshToken(User user, String ip, String userAgent) {

        String uuid = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(uuid)
                .ipAddress(ip)
                .userAgent(userAgent)
                .expiresAt(new Date(
                        Instant.now().plus(REFRESH_TOKEN_EXPIRY, ChronoUnit.DAYS).toEpochMilli()
                ))
                .user(user)
                .build();

        refreshTokenRepository.save(refreshToken);

        return RefreshTokenReturn.builder()
                .token(uuid)
                .expiry(REFRESH_TOKEN_EXPIRY)
                .build();
    }

    @Override
    public RefreshTokenReturn generateRefreshToken(String token, String ip, String userAgent) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        refreshToken.setIpAddress(ip);
        refreshToken.setUserAgent(userAgent);
        refreshToken.setExpiresAt(new Date(
                Instant.now().plus(REFRESH_TOKEN_EXPIRY, ChronoUnit.DAYS).toEpochMilli()
        ));

        refreshTokenRepository.save(refreshToken);

        String accessToken = authService.generateToken(refreshToken.getUser());

        return RefreshTokenReturn.builder()
                .token(refreshToken.getToken())
                .expiry(REFRESH_TOKEN_EXPIRY)
                .accessToken(accessToken)
                .accessTokenExpires(new Date(
                        Instant.now().plus(ACCESS_TOKEN_EXPIRY, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();
    }

    @Transactional
    @Override
    public void revokeToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
