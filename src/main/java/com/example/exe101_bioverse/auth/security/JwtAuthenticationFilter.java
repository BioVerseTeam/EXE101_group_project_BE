package com.example.exe101_bioverse.auth.security;

import com.example.exe101_bioverse.auth.service.JwtService;
import com.example.exe101_bioverse.auth.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthenticationFilter(JwtService jwtService, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        if (!jwtService.isValidAccessToken(token)) {
            writeUnauthorized(response, "Access token không hợp lệ hoặc đã hết hạn");
            return;
        }

        try {
            Claims claims = jwtService.parseClaims(token);
            Long userId = Long.parseLong(claims.getSubject());

            try {
                if (tokenBlacklistService.isBlacklisted(token, claims)
                        || tokenBlacklistService.isUserTokenRevoked(userId, claims.getIssuedAt())) {
                    writeUnauthorized(response, "Token đã bị vô hiệu hóa");
                    return;
                }
            } catch (Exception redisEx) {
                logger.warn("Redis blacklist check failed, allowing valid JWT: " + redisEx.getMessage());
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String email = claims.get("email", String.class);
                String role = claims.get("role", String.class);
                UserPrincipal principal = new UserPrincipal(userId, email, "", role, true);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("JWT filter processing error for token: " + e.getMessage(), e);
            writeUnauthorized(response, "Access token không hợp lệ");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(java.nio.charset.StandardCharsets.UTF_8.name());
        response.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"data\":null,\"code\":1008,\"message\":\"" + message + "\"}"
        );
    }
}
