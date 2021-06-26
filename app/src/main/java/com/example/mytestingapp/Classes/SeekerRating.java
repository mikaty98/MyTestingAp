package com.example.mytestingapp.Classes;

public class SeekerRating {

    String seekerName;
    String providerId;
    String review;
    float starNumber;

    public SeekerRating(String seekerName, String providerId, String review, float starNumber)
    {
        this.seekerName = seekerName;
        this.providerId = providerId;
        this.review = review;
        this.starNumber = starNumber;
    }

    public float getStarNumber() {
        return starNumber;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getseekerName() {
        return seekerName;
    }

    public String getReview() {
        return review;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setseekerName(String seekerName) {
        this.seekerName = seekerName;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setStarNumber(float starNumber) {
        this.starNumber = starNumber;
    }
}
