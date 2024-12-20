package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.LoginRequestModel;
import com.project.cryptowatcher.model.LoginResponseModel;
import com.project.cryptowatcher.model.RegisterRequestModel;

public interface AuthService {

    LoginResponseModel login(LoginRequestModel loginRequest);

    void register(RegisterRequestModel registerRequest);

    String refreshToken(String oldToken);

    String refreshRefreshToken(String oldRefreshToken);
}
