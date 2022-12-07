package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("CustomerCode")
    public String CustomerCode="";
    @SerializedName("CustomerName")
    public String CustomerName="";
    @SerializedName("LocationNo")
    public double LocationNo;
    @SerializedName("LocationName")
    public String LocationName="";
}
