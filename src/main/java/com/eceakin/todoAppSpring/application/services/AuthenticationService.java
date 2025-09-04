package com.eceakin.todoAppSpring.application.services;

import com.eceakin.todoAppSpring.application.dtos.auth.AuthenticationRequest;
import com.eceakin.todoAppSpring.application.dtos.auth.AuthenticationResponse;
import com.eceakin.todoAppSpring.application.dtos.auth.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}