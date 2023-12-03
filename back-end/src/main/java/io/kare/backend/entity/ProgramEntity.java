package io.kare.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = ProgramEntity.TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(columnNames = {ProgramEntity.NAME_COLUMN, UserEntity.ID_COLUMN})
})
public class ProgramEntity {
    public static final String TABLE_NAME = "programs";
    public static final String ID_COLUMN = "program_id";
    public static final String NAME_COLUMN = "name";
    public static final String DESCRIPTION_COLUMN = "description";

    private String id;
    private String name;
    private String description;
    private List<WorkoutEntity> workouts;
    private UserEntity user;

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

    @ManyToMany
    @JoinTable(
            name = "program_workouts",
            joinColumns = @JoinColumn(name = ProgramEntity.ID_COLUMN),
            inverseJoinColumns = @JoinColumn(name = WorkoutEntity.ID_COLUMN)
    )
    public List<WorkoutEntity> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<WorkoutEntity> workouts) {
        this.workouts = workouts;
    }

    @ManyToOne
    @JoinColumn(name = UserEntity.ID_COLUMN, nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
