package com.example.mytestingapp.Classes;

import androidx.annotation.Nullable;

import java.io.Serializable;

import kotlinx.coroutines.internal.LocalAtomics_commonKt;


public class LocalRequest implements Serializable
{


    String requestTitle;
    String requestDescription;
    String city;
    String suburb;
    String streetName;
    String streetNumber;
    String buildingName;
    String buildingNumber;
    String floorNumber;
    String apartmentNumber;
    String seekerName;
    String seekerID;

    public String getPicked() {
        return picked;
    }

    public void setPicked(String picked) {
        this.picked = picked;
    }

    String picked;



    public LocalRequest() {
    }

    public LocalRequest(String RequestTitle, String RequestDescription, String City, String Suburb, String StreetName, String StreetNumber, String BuildingName, String BuildingNumber, String FloorNumber, String ApartmentNumber,String seekerName,String seekerID, String picked)
    {
        requestTitle = RequestTitle;
        requestDescription = RequestDescription;
        city = City;
        suburb = Suburb;
        streetName = StreetName;
        streetNumber = StreetNumber;
        buildingName = BuildingName;
        buildingNumber = BuildingNumber;
        floorNumber = FloorNumber;
        apartmentNumber = ApartmentNumber;
        this.seekerName = seekerName;
        this.seekerID = seekerID;
        this.picked = picked;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getSeekerID() {
        return seekerID;
    }

    public void setSeekerID(String seekerID) {
        this.seekerID = seekerID;
    }

    public String getseekerName() {
        return seekerName;
    }

    public void setseekerName(String seekerName) {
        this.seekerName = seekerName;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof LocalRequest)){
            return false;
        }
        LocalRequest other = (LocalRequest) obj;
        return (seekerID == other.getSeekerID());
    }
}
