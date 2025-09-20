package com.tvt.task_mgmt.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tvt.task_mgmt.dto.request.AuthRequest;
import com.tvt.task_mgmt.dto.request.IntrospectRequest;
import com.tvt.task_mgmt.dto.request.LogoutRequest;
import com.tvt.task_mgmt.dto.response.MyApiResponse;
import com.tvt.task_mgmt.dto.response.AuthResponse;
import com.tvt.task_mgmt.dto.response.IntrospectResponse;
import com.tvt.task_mgmt.exception.ErrorCode;
import com.tvt.task_mgmt.entity.BlackListToken;
import com.tvt.task_mgmt.entity.User;
import com.tvt.task_mgmt.exception.AppException;
import com.tvt.task_mgmt.repository.InvalidateTokenRepository;
import com.tvt.task_mgmt.repository.UserRepository;
import com.tvt.task_mgmt.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidateTokenRepository invalidateTokenRepository;

    @NonFinal
    @Value("${jwt.secret-key}")
    protected String SECRET_KEY;

    @NonFinal
    @Value("${jwt.access-token-expiry}")
    protected int ACCESS_TOKEN_EXPIRY;

    @NonFinal
    @Value("${jwt.token-type}")
    protected String TOKEN_TYPE;

    @Override
    public AuthResponse authenticate(AuthRequest request) { //login
        var user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXIST));

        boolean matchPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matchPassword)
            throw new AppException(ErrorCode.WRONG_PASSWORD);

        String token = generateToken(user);
        return AuthResponse.builder()
                .user(user)
                .authenticated(true)
                .accessToken(token)
                .tokenExpiresAt(new Date(
                        Instant.now().plus(ACCESS_TOKEN_EXPIRY, ChronoUnit.HOURS).toEpochMilli()
                ))
                .tokenType(TOKEN_TYPE)
                .build();
    }

    @Override
    public MyApiResponse<IntrospectResponse> introspect(IntrospectRequest request) {
        try {
            SignedJWT verify = verifyToken(request.getToken());
            return MyApiResponse.of(
                    ErrorCode.SUCCESS,
                    IntrospectResponse.builder()
                            .valid(true)
                            .subject(verify.getJWTClaimsSet().getSubject())
                            .build()
            );
        } catch (ParseException e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public void logout(LogoutRequest request) {
        try {
            SignedJWT parsedToken = verifyToken(request.getToken());

            String jid = parsedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = parsedToken.getJWTClaimsSet().getExpirationTime();

            BlackListToken blackListToken = BlackListToken.builder()
                    .id(jid)
                    .expiryTime(expiryTime)
                    .build();

            invalidateTokenRepository.save(blackListToken);
        } catch (ParseException e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("tvt.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(ACCESS_TOKEN_EXPIRY, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject =new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("jwt sign error: ", e);
            throw new RuntimeException(e);
        }

    }

    private SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (!(expiryTime.after(new Date()) && signedJWT.verify(verifier)))
                throw new AppException(ErrorCode.VERIFICATION_FAILED);

            if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
                throw new AppException(ErrorCode.INVALIDATED_TOKEN);

            return signedJWT;
        } catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

}
