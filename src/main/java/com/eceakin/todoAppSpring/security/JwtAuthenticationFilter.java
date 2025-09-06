package com.eceakin.todoAppSpring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        log.debug("Processing request: {} {}", request.getMethod(), requestURI);
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        // Skip auth for public endpoints
        if (requestURI.startsWith("/api/auth/") || requestURI.startsWith("/api/debug/")) {
            log.debug("Skipping auth for public endpoint: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String jwt = parseJwt(request);
            
            if (jwt != null) {
                log.debug("JWT token found, length: {}", jwt.length());
                String username = jwtUtils.extractUsername(jwt);
                log.debug("Username from JWT: {}", username);
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    log.debug("User details loaded: {}", userDetails.getUsername());
                    
                    if (jwtUtils.validateToken(jwt, userDetails)) {
                        log.debug("JWT token is valid");
                        
                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("Authentication set for user: {}", username);
                    } else {
                        log.warn("JWT token validation failed for user: {}", username);
                    }
                }
            } else {
                log.debug("No JWT token found for request: {}", requestURI);
            }
        } catch (Exception e) {
            log.error("Authentication error for {}: {}", requestURI, e.getMessage(), e);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        log.debug("Authorization header: {}", headerAuth != null ? "Present (length: " + headerAuth.length() + ")" : "Not found");
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String token = headerAuth.substring(7);
            log.debug("Extracted token length: {}, starts with: {}", 
                     token.length(), 
                     token.length() > 10 ? token.substring(0, 10) : token);
            return token;
        }
        
        return null;
    }
}