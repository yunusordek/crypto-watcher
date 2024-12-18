package com.project.cryptowatcher.repository;

import com.project.cryptowatcher.entity.UserLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, Serializable> {

    Optional<UserLoginEntity> findByUsername(String username);
}
