package com.project.cryptowatcher.controller;

import com.project.cryptowatcher.model.ApiResponseDto;
import com.project.cryptowatcher.model.LoginRequestModel;
import com.project.cryptowatcher.model.RegisterRequestModel;
import com.project.cryptowatcher.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody LoginRequestModel loginRequest) {
        String token = userService.login(loginRequest);
        return ResponseEntity.ok(createResponse("Bearer " + token));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody RegisterRequestModel registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok(createResponse("User registered successfully"));
    }

    private <T> ApiResponseDto<T> createResponse(Object message) {
        ApiResponseDto<T> response = new ApiResponseDto<>();
        response.setResult(true);
        response.setMessage(message);
        return response;
    }
}
