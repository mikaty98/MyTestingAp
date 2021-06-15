package com.example.mytestingapp.Classes;

import java.io.Serializable;


public class GlobalRequest implements Serializable
{


    String requestTitle;
    String requestDescription;
    String itemCategory;
    String itemName;
    String itemSize;
    String itemBrandName;
    String city;
    String suburb;
    String streetName;
    String streetNumber;
    String buildingName;
    String buildingNumber;
    String floorNumber;
    String apartmentNumber;
    String seekerEmail;
    String seekerID;



    public GlobalRequest() {
    }

    public GlobalRequest(String RequestTitle, String RequestDescription, String itemCategory, String itemName, String itemSize, String itemBrandName, String City, String Suburb, String StreetName, String StreetNumber, String BuildingName, String BuildingNumber, String FloorNumber, String ApartmentNumber,String SeekerEmail,String seekerID)
    {
        this.requestTitle = RequestTitle;
        this.requestDescription = RequestDescription;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemSize = itemSize;
        this.itemBrandName = itemBrandName;
        this.city = City;
        this.suburb = Suburb;
        this.streetName = StreetName;
        this.streetNumber = StreetNumber;
        this.buildingName = BuildingName;
        this.buildingNumber = BuildingNumber;
        this.floorNumber = FloorNumber;
        this.apartmentNumber = ApartmentNumber;
        this.seekerEmail = SeekerEmail;
        this.seekerID = seekerID;
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

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemBrandName() {
        return itemBrandName;
    }

    public void setItemBrandName(String itemBrandName) {
        this.itemBrandName = itemBrandName;
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

    public String getSeekerEmail() {
        return seekerEmail;
    }

    public void setSeekerEmail(String seekerEmail) {
        this.seekerEmail = seekerEmail;
    }





}
