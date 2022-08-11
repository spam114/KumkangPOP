package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

public class SalesOrder {

    @SerializedName("SaleOrderNo")
    public String SaleOrderNo="";

    @SerializedName("LocationName")
    public String LocationName;

    @SerializedName("CustomerName")
    public String CustomerName;

    @SerializedName("Dong")
    public String Dong;

    @SerializedName("LocationNo")
    public int LocationNo;

    @SerializedName("CustomerCode")
    public String CustomerCode;
}