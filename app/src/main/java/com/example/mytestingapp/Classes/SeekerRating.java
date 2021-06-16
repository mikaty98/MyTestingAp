package com.example.mytestingapp.Classes;

public class SeekerRating {

    String seekerEmail;
    String providerId;
    String review;
    float starNumber;

    public SeekerRating(String seekerEmail, String providerId, String review, float starNumber)
    {
        this.seekerEmail = seekerEmail;
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

    public String getseekerEmail() {
        return seekerEmail;
    }

    public String getReview() {
        return review;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setseekerEmail(String seekerEmail) {
        this.seekerEmail = seekerEmail;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setStarNumber(float starNumber) {
        this.starNumber = starNumber;
    }
}
