package com.example.mytestingapp.Classes;

public class SeekerLocalRequestArrivalConfirm {

    private String userId, finalPrice;
    private  int flag;

    public SeekerLocalRequestArrivalConfirm(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getFlag()
    {
        return flag;
    }
    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }
}
