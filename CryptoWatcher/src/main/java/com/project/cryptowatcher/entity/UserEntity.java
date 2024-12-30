package com.project.cryptowatcher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "USER_INFORMATION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String email;

    @OneToMany(mappedBy = "userEntity")
    private List<FavoriteCoinEntity> favoriteCoins;

    @OneToOne(mappedBy = "userEntity")
    private UserLoginEntity userLogin;
}