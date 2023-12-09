package io.kare.backend.repository;

import io.kare.backend.entity.ExerciseEntity;
import io.kare.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, String> {
    List<ExerciseEntity> findAllByUser(UserEntity user);
    List<ExerciseEntity> findAllByUserId(String userId);
}
