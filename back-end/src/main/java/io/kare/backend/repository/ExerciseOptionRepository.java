package io.kare.backend.repository;

import io.kare.backend.entity.ExerciseOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseOptionRepository extends JpaRepository<ExerciseOptionEntity, String> {
}
