package com.example.application2;

public class Notification {
    private String name;
    private String description;

    public Notification(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
}
