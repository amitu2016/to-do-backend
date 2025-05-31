package com.example.todoapp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoapp.model.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.get("username"),
                    loginRequest.get("password")
                )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Invalid username or password"));
        }

        final String username = loginRequest.get("username");
        final User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of(
            "token", jwt,
            "id", user.getId(),
            "username", user.getUsername()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        if (userRepository.existsByUsername(registerRequest.get("username"))) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Username already exists"));
        }

        User user = new User(
            registerRequest.get("username"),
            passwordEncoder.encode(registerRequest.get("password"))
        );

        user = userRepository.save(user);

        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(registerRequest.get("username"));
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of(
            "token", jwt,
            "id", user.getId(),
            "username", user.getUsername()
        ));
    }
} 