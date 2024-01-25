package com.withus.be.common.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.be.dto.TokenDto;
import com.withus.be.dto.TokenDto.RefreshDto;
import com.withus.be.dto.TokenDto.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private Key key;
    private final String secret;
    private final long tokenValidityInMilliseconds;
    private final long refreshTokenValidityTime;
    private final RedisTemplate<String, RefreshDto> redisTemplate;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenValidityTime,
            RedisTemplate<String, RefreshDto> redisTemplate
    ) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.refreshTokenValidityTime = refreshTokenValidityTime * 1000;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        String refreshToken = String.valueOf(UUID.randomUUID());

        ValueOperations<String, RefreshDto> values = redisTemplate.opsForValue();
        values.set(refreshToken, RefreshDto.of(accessToken, authentication.getName(), authorities));
        redisTemplate.expire(refreshToken, refreshTokenValidityTime, TimeUnit.SECONDS);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessToken(Authentication authentication, String accessToken, String refreshToken) {
        if (!tokenValidationCheck(accessToken, refreshToken)) return "Token verification failed.";
        redisTemplate.delete(refreshToken);

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date((new Date()).getTime() + this.tokenValidityInMilliseconds))
                .compact();
    }

    private boolean tokenValidationCheck(String accessToken, String refreshToken) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(refreshToken))) return false;
        String actualValue = String.valueOf(redisTemplate.opsForValue().get(refreshToken));
        return actualValue.contains(accessToken);
    }

    @SuppressWarnings("unchecked")
    public TokenResponse generateTokenByRefreshToken(String accessToken, String refreshToken) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(refreshToken))) {
            return TokenResponse.of("Refresh Token에 해당하는 값이 없습니다.");
        }

        Object obj = redisTemplate.opsForValue().get(refreshToken);
        if (!String.valueOf(obj).contains(accessToken)) {
            return TokenResponse.of("Access Token이 일치하지 않습니다.");
        }
        redisTemplate.delete(refreshToken);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> value = objectMapper.convertValue(obj, Map.class);

        String email = value.get("email");
        String authority = value.get("authority");

        String newAccess = getAt(email, authority);
        String newRefresh = generateRefreshToken(newAccess, email, authority);

        return TokenResponse.of(newAccess, newRefresh);
    }

    private String getAt(String email, String authority) {
        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, authority)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date((new Date()).getTime() + this.tokenValidityInMilliseconds))
                .compact();
    }

    public String generateRefreshToken(String accessToken, String name, String authorities) {
        String refreshToken = String.valueOf(UUID.randomUUID());

        ValueOperations<String, RefreshDto> values = redisTemplate.opsForValue();
        values.set(refreshToken, RefreshDto.of(accessToken, name, authorities));
        redisTemplate.expire(refreshToken, refreshTokenValidityTime, TimeUnit.SECONDS);

        return refreshToken;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
