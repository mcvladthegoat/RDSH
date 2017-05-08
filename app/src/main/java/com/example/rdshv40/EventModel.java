package com.example.rdshv40;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class EventModel implements Serializable {
    @SerializedName("id")
    @Expose
    private Long eventId = 0L;

    @SerializedName("name")
    @Expose
    private String eventName = "";

    @SerializedName("date")
    @Expose
    private String eventDate = "";

    @SerializedName("description")
    @Expose
    private String eventDescription = "";

    //роль текущего пользователя в данном мероприятии
    //поиск среди списка участников?
    /*@SerializedName("userRole")
    @Expose
    private String userRole = "";*/

    //or UserModel?
    @SerializedName("participant")
    @Expose
    private List<UserModel> eventParticipant = null;

    //now -> name
    @SerializedName("direction")
    @Expose
    private List<DirectionModel> eventDirection = null;

    //or user model?
    @SerializedName("organizer")
    @Expose
    private UserModel eventOrganizer = null;

    //TODO Task List

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /*
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    */

    public List<UserModel> getEventParticipant() {
        return eventParticipant;
    }

    public void setEventParticipant(List<UserModel> eventParticipant) {
        this.eventParticipant = eventParticipant;
    }

    public List<DirectionModel> getEventDirection() {
        return eventDirection;
    }

    public void setEventDirection(List<DirectionModel> eventDirection) {
        this.eventDirection = eventDirection;
    }

    public UserModel getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(UserModel eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("eventName", eventName);
        hm.put("eventDate", eventDate);
        return hm;
    }
}
