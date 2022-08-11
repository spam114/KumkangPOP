package com.symbol.kumkangpop.model.object;

import com.google.gson.annotations.SerializedName;

public class Report {

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";

    @SerializedName("CarNumber")
    public String CarNumber="";

    @SerializedName("PartFlagName")
    public String PartFlagName="";

    @SerializedName("ActualWeight")
    public double ActualWeight;

    @SerializedName("LogicalWeight")
    public double LogicalWeight;

    @SerializedName("CustomerLocation")
    public String CustomerLocation="";

    @SerializedName("PartCode")
    public String PartCode="";

    @SerializedName("PartName")
    public String PartName="";

    @SerializedName("PartSpec")
    public String PartSpec="";

    @SerializedName("CustomerName")
    public String CustomerName="";

    @SerializedName("LocationNo")
    public String LocationNo="";

    @SerializedName("LocationName")
    public String LocationName="";

    @SerializedName("StockOutNo")
    public String StockOutNo="";

    @SerializedName("CommodityCode")
    public String CommodityCode="";

    @SerializedName("CommodityName")
    public String CommodityName="";

    @SerializedName("PackingNo")
    public String PackingNo="";

    @SerializedName("Dong")
    public String Dong="";

    @SerializedName("InQty")
    public double InQty;

    @SerializedName("InWeight")
    public double InWeight;

    @SerializedName("InVolumn")
    public double InVolumn;

    @SerializedName("ScrapQty")
    public double ScrapQty;

    @SerializedName("ScrapWeight")
    public double ScrapWeight;

    @SerializedName("ScrapVolumn")
    public double ScrapVolumn;

    @SerializedName("SettlementQty")
    public double SettlementQty;

    @SerializedName("SettlementWeight")
    public double SettlementWeight;

    @SerializedName("SettlementVolumn")
    public double SettlementVolumn;

    @SerializedName("Quantity1")
    public double Quantity1;

    @SerializedName("Quantity2")
    public double Quantity2;

    @SerializedName("Quantity3")
    public double Quantity3;

    @SerializedName("Quantity4")
    public double Quantity4;

    @SerializedName("LogicalWeight1")
    public double LogicalWeight1;

    @SerializedName("LogicalWeight2")
    public double LogicalWeight2;

    @SerializedName("LogicalWeight3")
    public double LogicalWeight3;

    @SerializedName("LogicalWeight4")
    public double LogicalWeight4;

    @SerializedName("Volumn1")
    public double Volumn1;

    @SerializedName("Volumn2")
    public double Volumn2;

    @SerializedName("Volumn3")
    public double Volumn3;

    @SerializedName("Volumn4")
    public double Volumn4;

    @SerializedName("repair_Quantity")
    public double repair_Quantity;

    @SerializedName("repair_M2")
    public double repair_M2;

    @SerializedName("repair_Kg")
    public double repair_Kg;

    @SerializedName("new_Quantity")
    public double new_Quantity;

    @SerializedName("new_M2")
    public double new_M2;

    @SerializedName("new_Kg")
    public double new_Kg;

    @SerializedName("sum_Quantity")
    public double sum_Quantity;

    @SerializedName("sum_M2")
    public double sum_M2;

    @SerializedName("sum_Kg")
    public double sum_Kg;

    @SerializedName("Qty")
    public double Qty;

    @SerializedName("M2")
    public double M2;

    @SerializedName("Weight")
    public double Weight;

    @SerializedName("ColorFlag")
    public boolean ColorFlag;

    @SerializedName("currentCQty")
    public double currentCQty;

    @SerializedName("currentAwaitQty")
    public double currentAwaitQty;

    @SerializedName("currentASaleQty")
    public double currentASaleQty;

    @SerializedName("currentAPlanQty")
    public double currentAPlanQty;

    @SerializedName("currentNSaleQty")
    public double currentNSaleQty;

    @SerializedName("currentNPlanQty")
    public double currentNPlanQty;

    @SerializedName("totalQty")
    public double totalQty;
}
