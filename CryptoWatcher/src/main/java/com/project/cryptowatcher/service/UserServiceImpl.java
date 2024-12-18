package com.project.cryptowatcher.service;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.entity.UserLoginEntity;
import com.project.cryptowatcher.model.LoginRequestModel;
import com.project.cryptowatcher.model.RegisterRequestModel;
import com.project.cryptowatcher.repository.UserLoginRepository;
import com.project.cryptowatcher.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserLoginRepository userLoginRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequestModel loginRequest) {
        UserLoginEntity user = userLoginRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException(ExceptionMessages.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException(ExceptionMessages.INVALID_CREDENTIALS);
        }
        return jwtTokenUtil.generateToken(user.getUsername());
    }

    @Override
    public void register(RegisterRequestModel registerRequest) {
        if (userLoginRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException(ExceptionMessages.USERNAME_ALL_READY_TAKEN);
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        UserLoginEntity newUser = UserLoginEntity.builder()
                .username(registerRequest.getUsername())
                .password(encodedPassword)
                .email(registerRequest.getEmail())
                .build();

        userLoginRepository.save(newUser);
    }
}

