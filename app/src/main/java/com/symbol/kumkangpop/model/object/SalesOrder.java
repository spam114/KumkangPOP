package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

public class SalesOrder {
    //@SerializedName("ErrorCheck")
    public String ErrorCheck = "";

    //@SerializedName("SaleOrderNo")
    public String SaleOrderNo = "";

    //@SerializedName("LocationName")
    public String LocationName;

    //@SerializedName("CustomerName")
    public String CustomerName;

    //@SerializedName("Dong")
    public String Dong;

    //@SerializedName("LocationNo")
    public double LocationNo;

    //@SerializedName("CustomerCode")
    public String CustomerCode;
    public double StockOutOrderQty;
    public double PackingCnt;
}