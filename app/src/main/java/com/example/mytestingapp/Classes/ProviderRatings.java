package com.example.mytestingapp.Classes;

public class ProviderRatings {

    String seekerId;
    String providerId;
    String review;
    float starNumber;

    public ProviderRatings(String seekerId, String providerId, String review, float starNumber)
    {
        this.seekerId = seekerId;
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

    public String getSeekerId() {
        return seekerId;
    }

    public String getReview() {
        return review;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
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


