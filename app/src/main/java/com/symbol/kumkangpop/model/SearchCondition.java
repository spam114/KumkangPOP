package com.symbol.kumkangpop.model;

import com.google.gson.annotations.SerializedName;

public class SearchCondition {
    @SerializedName("FromDate")
    public String FromDate;

    @SerializedName("ContractNo")
    public String ContractNo;

    @SerializedName("AppCode")
    public String AppCode;

    @SerializedName("AndroidID")
    public String AndroidID;

    @SerializedName("Model")
    public String Model;

    @SerializedName("PhoneNumber")
    public String PhoneNumber;

    @SerializedName("DeviceName")
    public String DeviceName;

    @SerializedName("DeviceOS")
    public String DeviceOS;

    @SerializedName("AppVersion")
    public String AppVersion;

    @SerializedName("Remark")
    public String Remark;

    @SerializedName("UserID")
    public String UserID;

    @SerializedName("PassWord")
    public String PassWord;

    @SerializedName("BusinessClassCode")
    public int BusinessClassCode;

    @SerializedName("CustomerCode")
    public String CustomerCode;

    @SerializedName("SaleOrderNo")
    public String SaleOrderNo;

    @SerializedName("PCCode")
    public String PCCode;

    @SerializedName("InDate")
    public String InDate;

    @SerializedName("DeptCode")
    public String DeptCode;

    @SerializedName("StockInType")
    public int StockInType;

    @SerializedName("CheckType")
    public int CheckType;

    @SerializedName("ShortNo")
    public String ShortNo;

    @SerializedName("SearchYear")
    public int SearchYear;

    @SerializedName("SearchMonth")
    public int SearchMonth;

    @SerializedName("SearchDay")
    public int SearchDay;

    @SerializedName("PartCode")
    public String PartCode;

    @SerializedName("StockInNo")
    public String StockInNo;

    @SerializedName("PartSpec")
    public String PartSpec;

    @SerializedName("InQty")
    public double InQty;

    @SerializedName("ActualWeight")
    public double ActualWeight;

    @SerializedName("WorkingGroup")
    public double WorkingGroup;

    @SerializedName("WorkingMachine")
    public double WorkingMachine;

    public SearchCondition(){}

}
