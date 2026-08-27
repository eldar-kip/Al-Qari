package com.example.ychicoran.dopclasses;

public class DataUpdatedEvent {
    public final String type; // Например, "language" или "riwayah"

    public DataUpdatedEvent(String type) {
        this.type = type;
    }
}
