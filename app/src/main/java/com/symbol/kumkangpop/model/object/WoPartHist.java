package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

public class WoPartHist {

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";

    @SerializedName("PartCode")
    public String PartCode="";

    @SerializedName("PartName")
    public String PartName="";

    @SerializedName("PartSpec")
    public String PartSpec="";

    @SerializedName("MSpec")
    public String MSpec="";

    @SerializedName("LogicalWeight")
    public double LogicalWeight;

    @SerializedName("WorksOrderNo")
    public String WorksOrderNo="";

    @SerializedName("WorderRequestNo")
    public String WorderRequestNo="";

    @SerializedName("ProductionType")
    public double ProductionType;

    @SerializedName("ReceivedQty")
    public double ReceivedQty;

    @SerializedName("WorderLot")
    public String WorderLot="";

    @SerializedName("BundelQty")
    public double BundelQty;

    @SerializedName("MakeDate")
    public String MakeDate="";

    @SerializedName("CheckDate")
    public String CheckDate="";

    @SerializedName("WorkDate")
    public String WorkDate="";

    @SerializedName("SaleOrderNo")
    public String SaleOrderNo="";

    @SerializedName("CustomerName")
    public String CustomerName="";

    @SerializedName("AssetsType")
    public double AssetsType;

}
