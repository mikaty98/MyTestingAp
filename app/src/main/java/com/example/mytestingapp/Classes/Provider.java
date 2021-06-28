package com.example.mytestingapp.Classes;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Provider {
    private String userName,jobDesc,gender,birthDay, birthMonth, birthYear, id,phoneNumber,email,password,userID;
    private Bitmap image;
    private boolean sentProposal, gotAccepted;

    protected Provider(Parcel in) {
        userName = in.readString();
        jobDesc = in.readString();
        gender = in.readString();
        birthDay = in.readString();
        birthMonth = in.readString();
        birthYear = in.readString();
        id = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        password = in.readString();
        userID = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
    }


    public Bitmap getImageBitmap() {
        return image;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.image = bitmap;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isSentProposal() {
        return sentProposal;
    }

    public void setSentProposal(boolean sentProposal) {
        this.sentProposal = sentProposal;
    }

    public boolean isGotAccepted() {
        return gotAccepted;
    }

    public void setGotAccepted(boolean gotAccepted) {
        this.gotAccepted = gotAccepted;
    }

    public Provider() {
        this.gotAccepted = false;
        this.sentProposal = false;
    }

    public Provider(String userName, String jobDesc, String gender, String birthDay, String birthMonth, String birthYear, String id, String phoneNumber, String email, String password) {
        this.userName = userName;
        this.jobDesc = jobDesc;
        this.gender = gender;
        this.birthDay = birthDay;
        this.birthMonth = birthMonth;
        this.birthYear = birthYear;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.gotAccepted = false;
        this.sentProposal = false;

    }


    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
}
