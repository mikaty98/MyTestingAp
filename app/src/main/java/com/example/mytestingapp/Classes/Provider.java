package com.example.mytestingapp.Classes;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Provider {
    private String userName,jobDesc,gender,age,id,phoneNumber,email,password;
    private Bitmap image;

    protected Provider(Parcel in) {
        userName = in.readString();
        jobDesc = in.readString();
        gender = in.readString();
        age = in.readString();
        id = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        password = in.readString();
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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



    public Provider() {
    }

    public Provider(String userName, String jobDesc, String gender, String age, String id, String phoneNumber, String email, String password) {
        this.userName = userName;
        this.jobDesc = jobDesc;
        this.gender = gender;
        this.age = age;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;

    }


}
