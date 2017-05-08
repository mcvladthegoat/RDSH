package com.example.rdshv40;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DirectionModel implements Serializable{

    @SerializedName("id")
    @Expose
    private Long directionId = 0L;

    @SerializedName("name")
    @Expose
    private String directionName = null;

    @SerializedName("description")
    @Expose
    private String directionDescription = null;

    @SerializedName("rating")
    @Expose
    private Integer directionRating = 0;



    public Long getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Long directionId) {
        this.directionId = directionId;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public String getDirectionDescription() {
        return directionDescription;
    }

    public void setDirectionDescription(String directionDescription) {
        this.directionDescription = directionDescription;
    }

    public Integer getDirectionRating() {
        return directionRating;
    }

    public void setDirectionRating(Integer directionRating) {
        this.directionRating = directionRating;
    }
}
