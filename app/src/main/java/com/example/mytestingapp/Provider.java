package com.example.mytestingapp;

import java.io.Serializable;

public class Provider implements Serializable {
    String userName,jobDesc,gender,age,id,phoneNumber,email,password;

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
