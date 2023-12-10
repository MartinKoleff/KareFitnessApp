package io.kare.backend.repository;

import io.kare.backend.entity.UserEntity;
import io.kare.backend.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface WorkoutRepository extends JpaRepository<WorkoutEntity, String> {

    List<WorkoutEntity> findAllByUser(UserEntity user);

    @Query("SELECT w FROM WorkoutEntity w WHERE w.id IN :ids AND w.user = :user")
    List<WorkoutEntity> findAllByIdsAndUser(
        @Param("ids") List<String> ids,
        @Param("user") UserEntity user
    );

}