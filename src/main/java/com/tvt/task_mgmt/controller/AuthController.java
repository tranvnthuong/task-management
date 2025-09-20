package com.tvt.task_mgmt.controller;

import com.tvt.task_mgmt.dto.request.AuthRequest;
import com.tvt.task_mgmt.dto.request.IntrospectRequest;
import com.tvt.task_mgmt.dto.request.LogoutRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.AuthResponse;
import com.tvt.task_mgmt.exception.ErrorCode;
import com.tvt.task_mgmt.dto.response.IntrospectResponse;
import com.tvt.task_mgmt.dto.return_method.RefreshTokenReturn;
import com.tvt.task_mgmt.service.AuthService;
import com.tvt.task_mgmt.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    RefreshTokenService refreshTokenService;

    @NonFinal
    @Value("${jwt.token-type}")
    protected String TOKEN_TYPE;

    @PostMapping("/log-in")
    MyApiResponse<AuthResponse> authenticate(@Valid @RequestBody AuthRequest requestBody,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {

        AuthResponse authResponse = authService.authenticate(requestBody);

        RefreshTokenReturn refreshTokenReturn = refreshTokenService.generateRefreshToken(
                authResponse.getUser(),
                getClientIp(request),
                request.getHeader("User-Agent")
        );

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshTokenReturn.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(Duration.ofDays(refreshTokenReturn.getExpiry()))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        authResponse.setUser(null);
        return MyApiResponse.of(ErrorCode.AUTHENTICATED, authResponse);
    }

    @PostMapping("/logout")
    MyApiResponse<Object> logout(@Valid @RequestBody LogoutRequest requestBody,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        String refreshToken = findRefreshTokenInCookies(request.getCookies());
        if (refreshToken != null) {
            refreshTokenService.revokeToken(refreshToken);
            ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/auth")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        }

        authService.logout(requestBody);
        return MyApiResponse.of(ErrorCode.LOGOUT);
    }

    @PostMapping("/introspect")
    MyApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request) {
        return authService.introspect(request);
    }

    @PostMapping("/refresh")
    MyApiResponse<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = findRefreshTokenInCookies(request.getCookies());
        RefreshTokenReturn refreshTokenReturn = refreshTokenService.generateRefreshToken(
                refreshToken,
                getClientIp(request),
                request.getHeader("User-Agent")
        );

        boolean existRefreshToken = refreshToken != null;

        ResponseCookie cookie = ResponseCookie.from("refresh_token",
                        existRefreshToken ? refreshTokenReturn.getToken() : "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(existRefreshToken
                        ? Duration.ofDays(refreshTokenReturn.getExpiry())
                        : Duration.ofMillis(1))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return MyApiResponse.of(
                ErrorCode.SUCCESS,
                AuthResponse.builder()
                        .authenticated(existRefreshToken)
                        .accessToken(existRefreshToken ? refreshTokenReturn.getAccessToken() : null)
                        .tokenExpiresAt(existRefreshToken ? refreshTokenReturn.getAccessTokenExpires() : null)
                        .tokenType(existRefreshToken ? TOKEN_TYPE : null)
                        .build()
        );
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String findRefreshTokenInCookies(Cookie[] cookies) {
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }
}
