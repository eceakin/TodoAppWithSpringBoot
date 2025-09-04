package com.eceakin.todoAppSpring.application.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.eceakin.todoAppSpring.application.dtos.auth.AuthenticationRequest;
import com.eceakin.todoAppSpring.application.dtos.auth.AuthenticationResponse;
import com.eceakin.todoAppSpring.application.dtos.auth.RegisterRequest;
import com.eceakin.todoAppSpring.application.services.AuthenticationService;
import com.eceakin.todoAppSpring.entities.User;
import com.eceakin.todoAppSpring.infrastructure.repositories.UserRepository;
import com.eceakin.todoAppSpring.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements AuthenticationService {
	   private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final JwtUtils jwtUtils;
	    private final AuthenticationManager authenticationManager;
	    
	    @Override
	    public AuthenticationResponse register(RegisterRequest request) {
	        // Check if user already exists
	        if (userRepository.existsByUsername(request.getUsername())) {
	            throw new IllegalArgumentException("Username is already taken!");
	        }
	        
	        if (userRepository.existsByEmail(request.getEmail())) {
	            throw new IllegalArgumentException("Email is already in use!");
	        }
	        
	        // Create new user
	        User user = new User();
	        user.setUsername(request.getUsername());
	        user.setEmail(request.getEmail());
	        user.setPassword(passwordEncoder.encode(request.getPassword()));
	        user.setFirstName(request.getFirstName());
	        user.setLastName(request.getLastName());
	        user.setEnabled(true);
	        
	        User savedUser = userRepository.save(user);
	        
	        // Generate JWT token
	        String jwt = jwtUtils.generateToken(savedUser);
	        
	        return new AuthenticationResponse(
	            jwt,
	            savedUser.getId(),
	            savedUser.getUsername(),
	            savedUser.getEmail(),
	            savedUser.getFirstName(),
	            savedUser.getLastName()
	        );
	    }
	    
	    @Override
	    public AuthenticationResponse authenticate(AuthenticationRequest request) {
	        authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                request.getUsername(),
	                request.getPassword()
	            )
	        );
	        
	        User user = userRepository.findByUsername(request.getUsername())
	                .orElseThrow(() -> new IllegalArgumentException("User not found"));
	        
	        String jwt = jwtUtils.generateToken(user);
	        
	        return new AuthenticationResponse(
	            jwt,
	            user.getId(),
	            user.getUsername(),
	            user.getEmail(),
	            user.getFirstName(),
	            user.getLastName()
	        );
	    }
	}
