package com.project.cryptowatcher.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseModel implements Serializable {

    private String accessToken;
    private String refreshToken;
}
