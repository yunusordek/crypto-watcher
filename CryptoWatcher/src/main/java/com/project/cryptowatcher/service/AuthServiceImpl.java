package com.project.cryptowatcher.service;

import com.project.cryptowatcher.constants.ExceptionMessages;
import com.project.cryptowatcher.entity.UserEntity;
import com.project.cryptowatcher.entity.UserLoginEntity;
import com.project.cryptowatcher.exception.InvalidCredentialsException;
import com.project.cryptowatcher.exception.TokenExpiredException;
import com.project.cryptowatcher.exception.UserNotFoundException;
import com.project.cryptowatcher.exception.UsernameAlreadyTakenException;
import com.project.cryptowatcher.mapper.UserLoginMapper;
import com.project.cryptowatcher.mapper.UserMapper;
import com.project.cryptowatcher.model.LoginRequestModel;
import com.project.cryptowatcher.model.LoginResponseModel;
import com.project.cryptowatcher.model.RegisterRequestModel;
import com.project.cryptowatcher.repository.UserLoginRepository;
import com.project.cryptowatcher.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserLoginRepository userLoginRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserLoginMapper userLoginMapper;
    private final UserMapper userMapper;

    @Override
    public LoginResponseModel login(LoginRequestModel loginRequest) {
        UserLoginEntity user = userLoginRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(ExceptionMessages.INVALID_CREDENTIALS);
        }
        return generateTokensAndSave(user.getUsername());
    }

    @Override
    @Transactional
    public void register(RegisterRequestModel registerRequest) {
        if (userLoginRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UsernameAlreadyTakenException(ExceptionMessages.USERNAME_ALREADY_TAKEN);
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        UserLoginEntity userLogin = userLoginMapper.toUserLoginEntity(registerRequest, encodedPassword);
        UserEntity userEntity = userMapper.toUserEntity(userLogin);
        userLogin.setUserEntity(userEntity);
        userEntity.setUserLogin(userLogin);
        userLoginRepository.save(userLogin);
    }

    @Override
    public String refreshToken(String oldToken) {
        var username = redisTemplate.opsForValue().get(oldToken);
        if (username == null || jwtTokenUtil.isTokenExpired(oldToken)) {
            throw new TokenExpiredException(ExceptionMessages.TOKEN_EXPIRED);
        }

        var newAccessToken = jwtTokenUtil.generateToken(username);
        updateToken(oldToken, newAccessToken, username, 1, TimeUnit.DAYS);

        return newAccessToken;
    }

    @Override
    public String refreshRefreshToken(String oldRefreshToken) {
        var username = redisTemplate.opsForValue().get(oldRefreshToken);
        if (username == null || jwtTokenUtil.isTokenExpired(oldRefreshToken)) {
            throw new TokenExpiredException(ExceptionMessages.TOKEN_EXPIRED);
        }

        var newRefreshToken = jwtTokenUtil.generateRefreshToken(username);
        updateToken(oldRefreshToken, newRefreshToken, username, 30, TimeUnit.DAYS);

        return newRefreshToken;
    }

    private LoginResponseModel generateTokensAndSave(String username) {
        String accessToken = jwtTokenUtil.generateToken(username);
        String refreshToken = jwtTokenUtil.generateRefreshToken(username);

        redisTemplate.opsForValue().set(accessToken, username, 1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(refreshToken, username, 30, TimeUnit.DAYS);

        return LoginResponseModel.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void updateToken(String oldToken, String newToken, String username, long timeout, TimeUnit unit) {
        redisTemplate.delete(oldToken);
        redisTemplate.opsForValue().set(newToken, username, timeout, unit);
    }
}