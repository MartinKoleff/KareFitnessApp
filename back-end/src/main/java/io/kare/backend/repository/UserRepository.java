package io.kare.backend.repository;

import io.kare.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

	boolean existsByEmail(String email);

}
