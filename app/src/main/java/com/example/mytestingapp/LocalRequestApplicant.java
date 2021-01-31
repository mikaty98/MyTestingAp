package com.example.mytestingapp;

public class LocalRequestApplicant {
    private float minPriceRange, maxPriceRange;
    private int estimatedTimeInHours;

    public LocalRequestApplicant(float minPriceRange, float maxPriceRange, int estimatedTimeInHours) {

        this.minPriceRange = minPriceRange;
        this.maxPriceRange = maxPriceRange;
        this.estimatedTimeInHours = estimatedTimeInHours;
    }

    public LocalRequestApplicant() {
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
