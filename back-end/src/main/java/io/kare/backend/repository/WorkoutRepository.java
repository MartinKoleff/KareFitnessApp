package io.kare.backend.repository;

import io.kare.backend.entity.UserEntity;
import io.kare.backend.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<WorkoutEntity, String> {
    List<WorkoutEntity> findAllByUser(UserEntity user);
}
