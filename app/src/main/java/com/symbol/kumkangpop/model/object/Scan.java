package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Scan {

    //@SerializedName("ErrorCheck")
    public String ErrorCheck="";

    //@SerializedName("ActivityFlag")
    public int ActivityFlag;

    //@SerializedName("SalesOrderList")
    public List<SalesOrder> SalesOrderList;

    public String Barcode="";

}
