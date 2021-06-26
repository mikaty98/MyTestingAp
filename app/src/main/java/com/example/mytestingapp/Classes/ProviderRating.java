package com.example.mytestingapp.Classes;

public class ProviderRating {

    String seekerId;
    String providerName;
    String review;
    float starNumber;

    public ProviderRating(String seekerId, String providerName, String review, float starNumber)
    {
        this.seekerId = seekerId;
        this.providerName = providerName;
        this.review = review;
        this.starNumber = starNumber;
    }

    public float getStarNumber() {
        return starNumber;
    }

    public String getproviderName() {
        return providerName;
    }

    public String getSeekerId() {
        return seekerId;
    }

    public String getReview() {
        return review;
    }

    public void setproviderName(String providerName) {
        this.providerName = providerName;
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


