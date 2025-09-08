package com.ncu.collage.model;

import java.time.OffsetDateTime;

public class User {
    private String userId;
    private String username;
    private String email;
    private String displayName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public User() {}

    public User(String userId, String username, String email, String displayName, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
