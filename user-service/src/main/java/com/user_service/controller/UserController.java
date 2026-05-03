package com.user_service.controller;

import com.user_service.config.JwtUtil;
import com.user_service.config.UserDetailsImpl;
import com.user_service.payload.request.LoginRequest;
import com.user_service.payload.request.UserRegistrationDto;
import com.user_service.payload.response.APIResponse;
import com.user_service.payload.response.AuthResponse;
import com.user_service.payload.response.UserDto;
import com.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserDto>> register(@Valid @RequestBody UserRegistrationDto dto) {
        UserDto user = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(HttpStatus.CREATED.value(), "User registered successfully", user));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtUtil.generateToken(auth);
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        AuthResponse resp = new AuthResponse(jwt, user.getId(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Login successful", resp));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Users fetched successfully", users));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserDto>> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(userDto -> ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "User fetched successfully", userDto))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(HttpStatus.NOT_FOUND.value(), "User not found")));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRegistrationDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "User updated successfully", updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new APIResponse<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully"));
    }
}
