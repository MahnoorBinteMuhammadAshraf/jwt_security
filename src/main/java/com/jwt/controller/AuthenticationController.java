package com.jwt.controller;

import com.jwt.domain.User;
import com.jwt.service.UserService;
import com.jwt.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping(value = "/auth")
@RestController
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/sign_in")
    public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password)
    {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userService.getUser(authenticate.getName());
        userService.setUsername(user.getUsername());

        String token = jwtUtil.generateToken(user.getUsername());
        String refresh_token = jwtUtil.generateRefreshToken(user.getUsername());


        Map<String, String> tokens = new HashMap<>();
        tokens.put("access-token", token);
        tokens.put("refresh_token", refresh_token);

        return ResponseEntity.ok(tokens);
    }

    @GetMapping(value = "/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token, HttpServletRequest request)
    {
        return ResponseEntity.ok("Refresh token expired");
    }

    @PostMapping(value = "/sign_up")
    public ResponseEntity<?> register(@RequestBody User user) {
       return ResponseEntity.ok().body(userService.createUser(user));
    }
}
