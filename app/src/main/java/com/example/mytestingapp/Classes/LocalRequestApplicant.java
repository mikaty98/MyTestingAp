package com.example.mytestingapp.Classes;

public class LocalRequestApplicant {
    private int priceValue, EstimatedArrivalTime;
    private int EstimatedCompletionTime;
    private String userID,providerEmail;

    public LocalRequestApplicant() {
    }

    public LocalRequestApplicant(int priceValue, int estimatedArrivalTime, int estimatedCompletionTime, String userID, String providerEmail) {
        this.priceValue = priceValue;
        EstimatedArrivalTime = estimatedArrivalTime;
        EstimatedCompletionTime = estimatedCompletionTime;
        this.userID = userID;
        this.providerEmail = providerEmail;
    }

    public int getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(int priceValue) {
        this.priceValue = priceValue;
    }

    public int getEstimatedArrivalTime() {
        return EstimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(int estimatedArrivalTime) {
        EstimatedArrivalTime = estimatedArrivalTime;
    }

    public int getEstimatedCompletionTime() {
        return EstimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(int estimatedCompletionTime) {
        EstimatedCompletionTime = estimatedCompletionTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }
}
