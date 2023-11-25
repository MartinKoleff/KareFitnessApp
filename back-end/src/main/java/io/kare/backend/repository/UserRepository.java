package io.kare.backend.repository;

import io.kare.backend.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

	boolean existsByEmail(String email);

	Optional<UserEntity> findByEmail(String email);

}
