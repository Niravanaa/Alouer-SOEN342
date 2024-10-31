package com.alouer.lessonManagement;

import java.time.LocalDateTime;

public class Booking {
    private Integer id;
    private Integer clientId;
    private Integer lessonId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Booking(int clientId, int lessonId) {
        this.clientId = clientId;
        this.lessonId = lessonId;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}