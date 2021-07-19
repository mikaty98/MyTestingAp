package com.example.mytestingapp.Classes;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class LocalRequestApplicant {
    private int priceValue, EstimatedArrivalTime;
    private int EstimatedCompletionTime;
    private String userID,providerName, deleted;

    public LocalRequestApplicant() {
    }

    public LocalRequestApplicant(int priceValue, int estimatedArrivalTime, int estimatedCompletionTime, String userID, String providerName, String deleted) {
        this.priceValue = priceValue;
        EstimatedArrivalTime = estimatedArrivalTime;
        EstimatedCompletionTime = estimatedCompletionTime;
        this.userID = userID;
        this.providerName = providerName;
        this.deleted = deleted;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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

    public String getproviderName() {
        return providerName;
    }

    public void setproviderName(String providerName) {
        this.providerName = providerName;
    }



    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (!(obj instanceof LocalRequestApplicant)){
            return false;
        }

        LocalRequestApplicant other = (LocalRequestApplicant) obj;
        return (userID == other.getUserID());
    }
}
