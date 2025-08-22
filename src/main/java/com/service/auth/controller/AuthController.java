package com.service.auth.controller;

import com.service.auth.dto.LoginRequest;
import com.service.auth.dto.LoginResponse;
import com.service.auth.dto.RegisterRequest;
import com.service.auth.entity.User;
import com.service.auth.security.JwtUtil;
import com.service.auth.security.UserDetailsImpl;
import com.service.auth.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private  final AuthService authService;

    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String jwt = jwtUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("USER");

        return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getId(), userDetails.getUsername(), role));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        String role = registerRequest.getRole() != null ? registerRequest.getRole() : "USER";

        authService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(), role);

        return ResponseEntity.ok("User registered successfully!");
    }
}