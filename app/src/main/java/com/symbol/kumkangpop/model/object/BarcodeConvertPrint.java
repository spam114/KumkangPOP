package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

public class BarcodeConvertPrint{

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";

    @SerializedName("Barcode")
    public String Barcode;

    @SerializedName("Result")
    public String Result;
}