package com.alouer.models.lessonManagement;

public class Booking {
    private Integer id;
    private Integer clientId;
    private Integer childId;
    private Integer lessonId;

    public Booking(Integer clientId, Integer lessonId, Integer childId) {
        this.clientId = clientId;
        this.lessonId = lessonId;
        if (childId != null) {
            this.childId = childId;
        }
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
}
