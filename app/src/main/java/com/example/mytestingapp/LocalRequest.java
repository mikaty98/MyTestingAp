package com.example.mytestingapp;

import java.io.Serializable;


public class LocalRequest implements Serializable
{


    String requestTitle,requestDescription,city,suburb,streetName,streetNumber,buildingName,buildingNumber,floorNumber,apartmentNumber;

    public LocalRequest() {
    }

    public LocalRequest(String RequestTitle, String RequestDescription, String City, String Suburb, String StreetName, String StreetNumber, String BuildingName, String BuildingNumber, String FloorNumber, String ApartmentNumber)
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






}
