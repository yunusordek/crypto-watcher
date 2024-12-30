package com.project.cryptowatcher.controller;

import com.project.cryptowatcher.model.ApiResponseDto;
import com.project.cryptowatcher.model.LoginRequestModel;
import com.project.cryptowatcher.model.LoginResponseModel;
import com.project.cryptowatcher.model.RegisterRequestModel;
import com.project.cryptowatcher.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<LoginResponseModel>> login(@RequestBody LoginRequestModel loginRequest) {
        return ResponseEntity.ok(createResponse(authService.login(loginRequest)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody RegisterRequestModel registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok(createResponse("User registered successfully"));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponseDto> refreshToken(@RequestHeader("Authorization") String oldToken) {
        var token = authService.refreshToken(oldToken.substring(7));
        return ResponseEntity.ok(createResponse(token));
    }

    @PostMapping("/refreshRefreshToken")
    public ResponseEntity<ApiResponseDto> refreshRefreshToken(@RequestHeader("Authorization") String oldRefreshToken) {
        var token = authService.refreshRefreshToken(oldRefreshToken.substring(7));
        return ResponseEntity.ok(createResponse(token));
    }

    private <T> ApiResponseDto<T> createResponse(Object message) {
        ApiResponseDto<T> response = new ApiResponseDto<>();
        response.setResult(true);
        response.setData(message);
        return response;
    }
}
