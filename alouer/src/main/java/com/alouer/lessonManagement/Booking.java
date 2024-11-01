package com.alouer.lessonManagement;

import java.time.LocalDateTime;

public class Booking {
    private Integer id;
    private Integer clientId;
    private Integer childId;
    private Integer lessonId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Booking(Integer clientId, Integer lessonId, Integer childId) {
        this.clientId = clientId;
        this.lessonId = lessonId;
        if (childId != null) {
            this.childId = childId;
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return this.id;
    }

    public Integer getChildId() {
        return this.childId;
    }

    public int getClientId() {
        return this.clientId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
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
