package com.project.cryptowatcher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PORTFOLIO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String portfolioName;

    private Double totalValue;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;
}
