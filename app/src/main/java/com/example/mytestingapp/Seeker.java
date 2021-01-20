package com.example.mytestingapp;

import android.os.Parcelable;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

public class Seeker implements Serializable {

    String UserName,Gender,Age,Id,PhoneNumber,Email,Password;



    public Seeker() {
    }

    public Seeker(String userName, String gender, String age, String id, String phoneNumber, String email, String password) {
        UserName = userName;
        Gender = gender;
        Age = age;
        Id = id;
        PhoneNumber = phoneNumber;
        Email = email;
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
