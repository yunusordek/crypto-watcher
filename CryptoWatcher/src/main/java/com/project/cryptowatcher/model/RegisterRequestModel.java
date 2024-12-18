package com.project.cryptowatcher.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisterRequestModel implements Serializable {

    private String username;
    private String password;
    private String email;

}