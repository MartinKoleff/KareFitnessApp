package io.kare.backend.payload.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MuscleGroup {
    CHEST("CHEST"),
    BACK("BACK"),
    SHOULDERS("SHOULDERS"),
    BICEPS("BICEPS"),
    TRICEPS("TRICEPS"),
    LEGS("LEGS"),
    ABS("ABS"),
    CARDIO("CARDIO");

    private final String value;

    MuscleGroup(String value) {
        this.value = value;
    }

    @JsonCreator
    public static MuscleGroup fromString(String value) {
        for (MuscleGroup muscleGroup : MuscleGroup.values()) {
            if (muscleGroup.value.equalsIgnoreCase(value)) {
                return muscleGroup;
            }
        }
        throw new IllegalArgumentException("Invalid MuscleGroup: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
