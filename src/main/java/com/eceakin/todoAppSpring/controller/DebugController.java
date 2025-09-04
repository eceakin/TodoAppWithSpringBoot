package com.eceakin.todoAppSpring.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@CrossOrigin(origins = "*")
@Slf4j
public class DebugController {
    
    @GetMapping("/test-auth")
    public ResponseEntity<Map<String, Object>> testAuth(HttpServletRequest request) {
        Map<String, Object> debugInfo = new HashMap<>();
        
        // Check Authorization header
        String authHeader = request.getHeader("Authorization");
        debugInfo.put("authorizationHeader", authHeader);
        debugInfo.put("hasAuthHeader", authHeader != null);
        
        if (authHeader != null) {
            debugInfo.put("startsWithBearer", authHeader.startsWith("Bearer "));
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                debugInfo.put("tokenLength", token.length());
            }
        }
        
        // Check Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        debugInfo.put("hasAuthentication", auth != null);
        debugInfo.put("isAuthenticated", auth != null && auth.isAuthenticated());
        
        if (auth != null) {
            debugInfo.put("principalType", auth.getPrincipal().getClass().getSimpleName());
            debugInfo.put("authorities", auth.getAuthorities().toString());
        }
        
        log.info("Debug info: {}", debugInfo);
        return ResponseEntity.ok(debugInfo);
    }
    
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Bu public endpoint - authentication gerekmez");
    }
}