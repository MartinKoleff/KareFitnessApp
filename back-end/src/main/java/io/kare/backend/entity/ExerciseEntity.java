package io.kare.backend.entity;

import io.kare.backend.payload.data.MachineType;
import io.kare.backend.payload.data.MuscleGroup;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = ExerciseEntity.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {ExerciseEntity.NAME_COLUMN, UserEntity.ID_COLUMN})
        }
)
public class ExerciseEntity {
    public static final String TABLE_NAME = "exercises";
    public static final String ID_COLUMN = "exercise_id";
    public static final String NAME_COLUMN = "name";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String MUSCLE_GROUP_COLUMN = "muscle_group";
    public static final String MACHINE_TYPE_COLUMN = "machine_type";
    public static final String URL_COLUMN = "url";

    private String id;
    private String name;
    private String description;
    private MuscleGroup muscleGroup;
    private MachineType machineType;
    private String url;
    private UserEntity user;
    private List<WorkoutEntity> workouts;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = ID_COLUMN, updatable = false, unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = NAME_COLUMN, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = DESCRIPTION_COLUMN)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = MUSCLE_GROUP_COLUMN, nullable = false)
    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = MACHINE_TYPE_COLUMN, nullable = false)
    public MachineType getMachineType() {
        return machineType;
    }

    public void setMachineType(MachineType machineType) {
        this.machineType = machineType;
    }

    @Column(name = URL_COLUMN)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = UserEntity.ID_COLUMN, nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @ManyToMany(mappedBy = "exercises")
    public List<WorkoutEntity> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<WorkoutEntity> workouts) {
        this.workouts = workouts;
    }
}
