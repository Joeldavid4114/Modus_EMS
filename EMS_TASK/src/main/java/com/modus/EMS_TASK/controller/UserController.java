package com.modus.EMS_TASK.controller;

import com.modus.EMS_TASK.dto.LoginDto;
import com.modus.EMS_TASK.dto.UserRegistrationDto;
import com.modus.EMS_TASK.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto dto) {
        userService.register(dto);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok(Map.of("token", token));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        // Optional: Log the token or blacklist it
        // tokenService.blacklist(token);

        return ResponseEntity.ok("Logged out successfully");
    }

}

