package com.example.mytestingapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Provider implements Parcelable {
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

    public static final Creator<Provider> CREATOR = new Creator<Provider>() {
        @Override
        public Provider createFromParcel(Parcel in) {
            return new Provider(in);
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };

    public Bitmap getImageBitmap() {
        return image;
    }

    public void setImageBitmap(Bitmap imageUri) {
        this.image = imageUri;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(jobDesc);
        dest.writeString(gender);
        dest.writeString(age);
        dest.writeString(id);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeParcelable(image, flags);
    }
}
