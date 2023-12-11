package io.kare.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = SelectedWorkoutEntity.TABLE_NAME)
public class SelectedWorkoutEntity {

	public static final String TABLE_NAME = "selected_workout";
	public static final String ID_COLUMN = "id";
	public static final String WORKOUT_COLUMN = "workout_id";
	public static final String USER_COLUMN = "user_id";

	private String id;

	private WorkoutEntity workout;

	private UserEntity user;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	public String getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name = WORKOUT_COLUMN)
	public WorkoutEntity getWorkout() {
		return workout;
	}

	@ManyToOne
	@JoinColumn(name = USER_COLUMN)
	public UserEntity getUser() {
		return user;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setWorkout(WorkoutEntity workout) {
		this.workout = workout;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
}
