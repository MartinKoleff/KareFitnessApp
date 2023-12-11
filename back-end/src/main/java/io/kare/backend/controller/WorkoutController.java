package io.kare.backend.controller;

import io.kare.backend.annotation.User;
import io.kare.backend.entity.UserEntity;
import io.kare.backend.payload.request.*;
import io.kare.backend.payload.response.AddWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutResponse;
import io.kare.backend.payload.response.GetWorkoutsResponse;
import io.kare.backend.service.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping("/add")
    public ResponseEntity<AddWorkoutResponse> addWorkout(@RequestBody AddWorkoutRequest request, @User UserEntity user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.workoutService.addWorkout(request, user));
    }

    @PutMapping("/select")
    public ResponseEntity<?> selectWorkout(@RequestBody SelectWorkoutRequest request, @User UserEntity user) {
        this.workoutService.selectWorkout(request, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all")
    public ResponseEntity<GetWorkoutsResponse> getWorkouts(@User UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.workoutService.getWorkouts(user));
    }

    @GetMapping("/get")
    public ResponseEntity<GetWorkoutResponse> getWorkout(@RequestBody GetWorkoutRequest request, @User UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.workoutService.getWorkout(request, user));
    }

    @GetMapping("/selected")
    public ResponseEntity<GetWorkoutResponse> getSelectedWorkout(@User UserEntity user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.workoutService.getSelectedWorkout(user));
    }

}
