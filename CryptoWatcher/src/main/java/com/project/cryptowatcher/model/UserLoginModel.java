package com.project.cryptowatcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginModel implements Serializable {

    private int id;
    private String userName;
    private String email;
    private String password;

}
