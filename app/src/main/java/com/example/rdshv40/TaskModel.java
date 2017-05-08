package com.example.rdshv40;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class TaskModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Long taskId = 0L;

    @SerializedName("name")
    @Expose
    private String taskName = "";

    @SerializedName("date")
    @Expose
    private String taskDate = "";

    @SerializedName("description")
    @Expose
    private String taskDescription = "";

    //or UserModel?
    @SerializedName("participant")
    @Expose
    private List<UserModel> eventParticipant = null;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public List<UserModel> getEventParticipant() {
        return eventParticipant;
    }

    public void setEventParticipant(List<UserModel> eventParticipant) {
        this.eventParticipant = eventParticipant;
    }
}
