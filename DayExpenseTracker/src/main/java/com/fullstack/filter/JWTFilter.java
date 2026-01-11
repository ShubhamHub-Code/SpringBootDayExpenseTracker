package com.fullstack.filter;

import com.fullstack.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        log.info("JWTFilter executed for URI: {}", request.getRequestURI());
        log.info("Authorization Header: {}", authHeader);

        // ✅ 1. If no Authorization header → PUBLIC request → continue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String userEmail;

        try {
            // ✅ 2. Extract username from token
            userEmail = jwtUtil.extractUsername(token);
        } catch (ExpiredJwtException e) {
            log.warn("JWT Token expired");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT Token expired");
            return;
        } catch (MalformedJwtException | SignatureException e) {
            log.warn("Invalid JWT Token");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT Token");
            return;
        } catch (Exception e) {
            log.error("JWT processing error", e);
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "JWT processing error");
            return;
        }

        // ✅ 3. Authenticate user if not already authenticated
        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(userEmail);

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("JWT validated successfully for user: {}", userEmail);
            } else {
                log.warn("JWT validation failed");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT Token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
