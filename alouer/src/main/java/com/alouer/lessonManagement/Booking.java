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

    public Integer getId() {
        return this.id;
    }

    public Integer getChildId() {
        return this.childId;
    }

    public Integer getClientId() {
        return this.clientId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public void setLessonId(Integer lessonId) {
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
