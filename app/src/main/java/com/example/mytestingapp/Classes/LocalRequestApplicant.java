package com.example.mytestingapp.Classes;

public class LocalRequestApplicant {
    private int priceValue, EstimatedArrivalTime;
    private int EstimatedCompletionTime;
    private String providerEmail;

    public LocalRequestApplicant(int priceValue, int EstimatedArrivalTime, int EstimatedCompletionTime,String providerEmail) {

        this.priceValue = priceValue;
        this.EstimatedArrivalTime = EstimatedArrivalTime;
        this.EstimatedCompletionTime = EstimatedCompletionTime;
        this.providerEmail = providerEmail;
    }

    public LocalRequestApplicant() {
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
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

    public void setEstimatedArrivalTime(int maxPriceRange) {
        this.EstimatedArrivalTime = maxPriceRange;
    }

    public int getEstimatedCompletionTime() {
        return EstimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(int EstimatedCompletionTime) {
        this.EstimatedCompletionTime = EstimatedCompletionTime;
    }
}
