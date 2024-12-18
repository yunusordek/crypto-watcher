package com.project.cryptowatcher.service;

import com.project.cryptowatcher.model.LoginRequestModel;
import com.project.cryptowatcher.model.RegisterRequestModel;

public interface UserService {

    String login(LoginRequestModel loginRequest);

    void register(RegisterRequestModel registerRequest);
}
