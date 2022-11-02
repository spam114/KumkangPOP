package com.symbol.kumkangpop.model;

import com.google.gson.annotations.SerializedName;
import com.symbol.kumkangpop.model.object.ScanListViewItem;

import java.util.List;

public class SearchCondition {
    @SerializedName("FromDate")
    public String FromDate;

    @SerializedName("ToDate")
    public String ToDate;

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

    @SerializedName("SaleType")
    public String SaleType;

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

    @SerializedName("Type")
    public int Type;

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

    @SerializedName("WorkDate")
    public String WorkDate;

    @SerializedName("WorderLot")
    public String WorderLot;

    @SerializedName("WorksOrderNo")
    public String WorksOrderNo;

    @SerializedName("WorderRequestNo")
    public String WorderRequestNo;

    @SerializedName("MSpec")
    public String MSpec;

    @SerializedName("ProductionType")
    public int ProductionType;

    @SerializedName("ReceivedQty")
    public double ReceivedQty;

    @SerializedName("AssetsType")
    public int AssetsType;

    @SerializedName("BundelQty")
    public double BundelQty;

    @SerializedName("PartFlag")
    public int PartFlag;

    @SerializedName("SpecFlag")
    public int SpecFlag;

    @SerializedName("WeightPrint")
    public boolean WeightPrint;

    @SerializedName("CommodityCode")
    public String CommodityCode;

    @SerializedName("Barcode")
    public String Barcode;

    @SerializedName("LocationNo")
    public int LocationNo;

    @SerializedName("PrintNum")
    public int PrintNum;

    @SerializedName("Ho")
    public String Ho;

    //IConvetDivision
    @SerializedName("IConvetDivision")
    public double IConvetDivision;

    @SerializedName("HoList")
    public List<String> HoList;

    @SerializedName("PackingNo")
    public String PackingNo;

    @SerializedName("PrintDivision")
    public int PrintDivision;

    @SerializedName("PrintNo")
    public String PrintNo;

    @SerializedName("Language")
    public int Language;

    @SerializedName("NType")
    public String NType;

    @SerializedName("ScanList")
    public List<ScanListViewItem> ScanList;

    @SerializedName("SaleOrderQty")
    public double SaleOrderQty;

    @SerializedName("PackingQty")
    public double PackingQty;

    @SerializedName("ScanQty")
    public double ScanQty;

    @SerializedName("WorkDiv")
    public int WorkDiv;

    @SerializedName("Tag")
    public String Tag;

    @SerializedName("ErrorDiv")
    public int ErrorDiv;

    public SearchCondition(){}

}
