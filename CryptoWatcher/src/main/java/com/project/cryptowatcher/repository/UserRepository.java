package com.project.cryptowatcher.repository;

import com.project.cryptowatcher.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Serializable> {

    Optional<UserEntity> findByUsername(String username);
}
