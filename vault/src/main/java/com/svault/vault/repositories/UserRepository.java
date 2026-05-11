package com.svault.vault.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.svault.vault.models.entity.UserEntity;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    public Optional<UserEntity> findById(Integer id);
    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
    UserEntity findByEmail(String email);
    List<UserEntity> findAllByRole(String role);
}
