package com.example.demo.Jwt;


import com.example.demo.security.CustomUserDetails;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${demo.app.jwtSecret}")
    private String JWT_SECRET;
    @Value("${demo.app.jwtExpirationMs}")
    private int JWT_EXPIRATION;

    //Tao jwt tu thong tin cua User
    //tu customUserDratils generate thanh chuoi jwt

    public String generateToken(CustomUserDetails customUserDetails) {
        Date now = new Date();
        Date dateExpired = new Date(now.getTime() + JWT_EXPIRATION);

        //Tao chuoi JSON WEB TOKEN tu username  cua user
        return Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(dateExpired)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    //Lay thong tin user tu jwt
    public String getUserNameFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET)
                .parseClaimsJws(token).getBody();
        //tra lai thong tin username
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET)
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT Token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims String is empty");
        }
        return false;
    }

    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }
    
}
