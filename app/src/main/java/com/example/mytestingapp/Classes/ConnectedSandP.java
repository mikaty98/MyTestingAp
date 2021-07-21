package com.example.mytestingapp.Classes;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ConnectedSandP {
    private String seekerID, providerID;
    private int startFlag;
    private String userType;
    private int completionTime, arrivalTime, price;
    private DatabaseReference reference;


    public ConnectedSandP(String seekerID, String providerID, int completionTime, String userType, int arrivalTime, int price) {
        this.seekerID = seekerID;
        this.providerID = providerID;
        this.startFlag = 0; //false
        this.completionTime = completionTime;
        this.userType = userType;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.reference = FirebaseDatabase.getInstance().getReference("StartingConnections");
    }

    public String getSeekerID() {
        return seekerID;
    }

    public void setSeekerID(String seekerID) {
        this.seekerID = seekerID;
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public int getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(int startFlag) {
        this.startFlag = startFlag;
    }

    public void checkStartFlag() {
        Query checkuser = reference.orderByChild("seekerID").equalTo(seekerID);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    startFlag = snapshot.child(seekerID+providerID).child("startFlag").getValue(int.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
