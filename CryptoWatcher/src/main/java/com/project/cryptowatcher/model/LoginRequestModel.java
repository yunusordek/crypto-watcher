package com.project.cryptowatcher.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequestModel implements Serializable {

    private String username;
    private String password;

}