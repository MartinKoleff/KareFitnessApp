package io.kare.backend.repository;

import io.kare.backend.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<WorkoutEntity, String> {
}
