package com.example.rdshv40;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {

    @SerializedName("loginResult")
    @Expose
    private String loginResult = "";

    @SerializedName("registResult")
    @Expose
    private String registResult = "";

    @SerializedName("id")
    @Expose
    private Long personId = 0L;
    @SerializedName("role")
    @Expose
    private String personRole = "";

    @SerializedName("name")
    @Expose
    private String personName = "";

    @SerializedName("lastName")
    @Expose
    private String personLastName = "";

    @SerializedName("middleName")
    @Expose
    private String personMiddleName = "";

    @SerializedName("email")
    @Expose
    private String personEmail = "";

    @SerializedName("rating")
    @Expose
    private Integer personRating = 0;

    @SerializedName("telephone")
    @Expose
    private String personTelephone = "";

    @SerializedName("birthday")
    @Expose
    private String personBirthday = "";

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public String getRegistResult() {
        return registResult;
    }

    public void setRegistResult(String registResult) {
        this.registResult = registResult;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getPersonRole() {
        return personRole;
    }

    public void setPersonRole(String personRole) {
        this.personRole = personRole;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public String getPersonMiddleName() {
        return personMiddleName;
    }

    public void setPersonMiddleName(String personMiddleName) {
        this.personMiddleName = personMiddleName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public Integer getPersonRating() {
        return personRating;
    }

    public void setPersonRating(Integer personRating) {
        this.personRating = personRating;
    }

    public String getPersonTelephone() {
        return personTelephone;
    }

    public void setPersonTelephone(String personTelephone) {
        this.personTelephone = personTelephone;
    }

    public String getPersonBirthday() {
        return personBirthday;
    }

    public void setPersonBirthday(String personBirthday) {
        this.personBirthday = personBirthday;
    }
}
