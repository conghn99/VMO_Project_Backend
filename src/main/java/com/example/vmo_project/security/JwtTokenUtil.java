package com.example.vmo_project.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtil {
    // Token có hạn trong vòng 24 giờ kể từ thời điểm tạo, thời gian tính theo giây
    private static final Integer duration = 60 * 60 * 24;

    // Key này sẽ được sử dụng để mã hóa và giải mã
    private static final String secret = "xin-chao-cac-ban-toi-ten-la-jwt-hihihihihi";
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    // Sinh token
    public String generateToken(UserDetails userDetails) {
        // Lưu thông tin Authorities của user vào claims
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("authorities", userDetails.getAuthorities());

        // 1. Định nghĩa các claims: issuer, expiration, subject, id
        // 2. Mã hóa token sử dụng thuật toán và key bí mật
        return Jwts.builder()
//                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration * 1000))
                .signWith(key)
                .compact();
    }

    // Lấy thông tin được lưu trong token
    public Claims getClaimsFromToken(String token) {
        // Kiểm tra token có bắt đầu bằng tiền tố
        if (token == null) return null;
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            return !Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken).getBody().getSubject().isBlank();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}