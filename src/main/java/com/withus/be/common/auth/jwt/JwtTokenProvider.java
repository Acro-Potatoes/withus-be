package com.withus.be.common.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.be.common.exception.InvalidParameterException;
import com.withus.be.dto.TokenDto;
import com.withus.be.dto.TokenDto.RefreshDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.withus.be.common.auth.oauth.OAuthOption.AUTHORITIES_KEY;

@Slf4j
@Component
public class JwtTokenProvider {

    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;
    private final JwtTokenValidator jwtTokenValidator;
    private final RedisTemplate<String, RefreshDto> redisTemplate;

    public JwtTokenProvider(
            JwtTokenValidator jwtTokenValidator,
            @Value("${jwt.token-validity-in-seconds}") long accessTokenValidityTime,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenValidityTime,
            RedisTemplate<String, RefreshDto> redisTemplate
    ) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.accessTokenValidityTime = accessTokenValidityTime * 1000;
        this.refreshTokenValidityTime = refreshTokenValidityTime * 1000;
        this.redisTemplate = redisTemplate;
    }

    public TokenDto generateToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);
        String email = authentication.getName();
        String accessToken = buildAccessToken(email, authorities);
        String refreshToken = String.valueOf(UUID.randomUUID());

        setRefreshToRedis(refreshToken, accessToken, email, authorities);

        return buildTokenDto(accessToken, refreshToken);
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private void setRefreshToRedis(String refreshToken, String accessToken, String email, String authorities) {
        ValueOperations<String, RefreshDto> values = redisTemplate.opsForValue();
        values.set(refreshToken, RefreshDto.of(accessToken, email, authorities));
        redisTemplate.expire(refreshToken, refreshTokenValidityTime, TimeUnit.SECONDS);
    }

    @SuppressWarnings("unchecked")
    public TokenDto generateTokenByRefreshToken(String accessToken, String refreshToken) {
        Object obj = tokenValidCheck(accessToken, refreshToken);

        Map<String, String> value = new ObjectMapper().convertValue(obj, Map.class);
        String email = value.get("email");
        String authority = value.get("authority");

        String newAccess = buildAccessToken(email, authority);
        String newRefresh = generateRefreshToken(newAccess, email, authority);

        return buildTokenDto(newAccess, newRefresh);
    }

    private Object tokenValidCheck(String accessToken, String refreshToken) {
        refreshCheck(refreshToken);
        Object obj = redisTemplate.opsForValue().get(refreshToken);
        accessCheck(obj, accessToken);
        redisTemplate.delete(refreshToken);
        return obj;
    }

    private void accessCheck(Object obj, String accessToken) {
        if (!Objects.requireNonNull(obj).toString().contains(accessToken))
            throw new InvalidParameterException("Access Token이 일치하지 않습니다.");
    }

    private void refreshCheck(String refreshToken) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(refreshToken)))
            throw new InvalidParameterException("Refresh Token에 해당하는 값이 없습니다.");
    }

    private String buildAccessToken(String email, String authority) {
        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, authority)
                .signWith(jwtTokenValidator.getKey(), SignatureAlgorithm.HS512)
                .setExpiration(new Date((new Date()).getTime() + this.accessTokenValidityTime))
                .compact();
    }

    private String generateRefreshToken(String accessToken, String email, String authorities) {
        String refreshToken = String.valueOf(UUID.randomUUID());
        setRefreshToRedis(refreshToken, accessToken, email, authorities);
        return refreshToken;
    }

    private TokenDto buildTokenDto(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
