package com.example.mytestingapp.Classes;

public class ProviderRating {

    String seekerId;
    String providerEmail;
    String review;
    float starNumber;

    public ProviderRating(String seekerId, String providerEmail, String review, float starNumber)
    {
        this.seekerId = seekerId;
        this.providerEmail = providerEmail;
        this.review = review;
        this.starNumber = starNumber;
    }

    public float getStarNumber() {
        return starNumber;
    }

    public String getproviderEmail() {
        return providerEmail;
    }

    public String getSeekerId() {
        return seekerId;
    }

    public String getReview() {
        return review;
    }

    public void setproviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setStarNumber(float starNumber) {
        this.starNumber = starNumber;
    }
}


