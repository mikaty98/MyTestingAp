package com.example.mytestingapp.Classes;

public class LocalRequestApplicant {
    private float minPriceRange, maxPriceRange;
    private int estimatedTimeInHours;
    private String providerEmail;

    public LocalRequestApplicant(float minPriceRange, float maxPriceRange, int estimatedTimeInHours,String providerEmail) {

        this.minPriceRange = minPriceRange;
        this.maxPriceRange = maxPriceRange;
        this.estimatedTimeInHours = estimatedTimeInHours;
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

    public float getMinPriceRange() {
        return minPriceRange;
    }

    public void setMinPriceRange(float minPriceRange) {
        this.minPriceRange = minPriceRange;
    }

    public float getMaxPriceRange() {
        return maxPriceRange;
    }

    public void setMaxPriceRange(float maxPriceRange) {
        this.maxPriceRange = maxPriceRange;
    }

    public int getEstimatedTimeInHours() {
        return estimatedTimeInHours;
    }

    public void setEstimatedTimeInHours(int estimatedTimeInHours) {
        this.estimatedTimeInHours = estimatedTimeInHours;
    }
}
