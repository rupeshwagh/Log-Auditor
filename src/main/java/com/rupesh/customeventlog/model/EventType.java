package com.rupesh.customeventlog.model;

import java.util.Arrays;

public enum EventType {
	
    APPLICATION_LOG("APPLICATION_LOG");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EventType findEventType(String text) {
        return Arrays.stream(values()).filter(v -> v.getValue().equals(text)).findFirst().orElse(null);
    }
}
