package com.b12kab.tmdblibrary.enumerations;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public enum StatusState {
    RUMORED("Rumored"),
    PLANNED("Planned"),
    IN_PRODUCTION("In Production"),
    POST_PRODUCTION("Post Production"),
    RELEASED("Released"),
    CANCELLED("Cancelled");

    public final String value;

    StatusState(String value) {
        this.value = value;
    }

    private static final Map<String, StatusState> STRING_MAPPING = new HashMap<>();

    static {
        for (StatusState via : StatusState.values()) {
            STRING_MAPPING.put(via.toString().toUpperCase(), via);
        }
    }

    public static StatusState fromValue(String value) {
        return STRING_MAPPING.get(value.toUpperCase());
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }
}
