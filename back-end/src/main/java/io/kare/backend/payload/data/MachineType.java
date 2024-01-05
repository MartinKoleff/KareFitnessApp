package io.kare.backend.payload.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MachineType {
    BARBELL("BARBELL"),
    DUMBBELL("DUMBBELL"),
    CABLE("CABLE"),
    MACHINE("MACHINE"),
    BODYWEIGHT("BODYWEIGHT");

    private final String value;

    MachineType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static MachineType fromString(String value) {
        for (MachineType machineType : MachineType.values()) {
            if (machineType.value.equalsIgnoreCase(value)) {
                return machineType;
            }
        }
        throw new IllegalArgumentException("Invalid MachineType: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
