package com.eceakin.todoAppSpring.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eceakin.todoAppSpring.application.dtos.auth.AuthenticationRequest;
import com.eceakin.todoAppSpring.application.dtos.auth.AuthenticationResponse;
import com.eceakin.todoAppSpring.application.dtos.auth.RegisterRequest;
import com.eceakin.todoAppSpring.application.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
	 private final AuthenticationService authenticationService;
	    
	    @PostMapping("/login")
	    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest loginRequest) {
	        AuthenticationResponse response = authenticationService.authenticate(loginRequest);
	        return ResponseEntity.ok(response);
	    }
	    
	    @PostMapping("/register")
	    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
	        AuthenticationResponse response = authenticationService.register(signUpRequest);
	        return ResponseEntity.ok(response);
	    }
}
