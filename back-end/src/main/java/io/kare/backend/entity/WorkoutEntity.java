package io.kare.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = WorkoutEntity.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(columnNames = {WorkoutEntity.NAME_COLUMN, UserEntity.ID_COLUMN})
})
public class WorkoutEntity {
    public static final String TABLE_NAME = "workouts";
    public static final String ID_COLUMN = "workout_id";
    public static final String NAME_COLUMN = "name";
    public static final String DESCRIPTION_COLUMN = "description";

    private String id;
    private String name;
    private String description;
    private UserEntity user;
    private List<ExerciseEntity> exercises;
    private List<ProgramEntity> programs;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = ID_COLUMN, updatable = false, unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = UserEntity.ID_COLUMN, nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Column(name = NAME_COLUMN, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = DESCRIPTION_COLUMN, nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany
    @JoinTable(
            name = "workout_exercises",
            joinColumns = @JoinColumn(name = WorkoutEntity.ID_COLUMN),
            inverseJoinColumns = @JoinColumn(name = ExerciseEntity.ID_COLUMN)
    )
    public List<ExerciseEntity> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseEntity> exercises) {
        this.exercises = exercises;
    }

    @ManyToMany(mappedBy = "workouts")
    public List<ProgramEntity> getPrograms() {
        return programs;
    }

    public void setPrograms(List<ProgramEntity> programs) {
        this.programs = programs;
    }

}
