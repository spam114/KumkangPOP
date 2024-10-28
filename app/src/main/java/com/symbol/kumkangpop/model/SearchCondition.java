package com.symbol.kumkangpop.model;

import com.google.gson.annotations.SerializedName;
import com.symbol.kumkangpop.model.object.ScanListViewItem;
import com.symbol.kumkangpop.model.object.ScanListViewItem2;
import com.symbol.kumkangpop.model.object.StockOutDetail;

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

    @SerializedName("ScanList2")
    public List<ScanListViewItem2> ScanList2;

    @SerializedName("ScanListTemp")
    public List<ScanListViewItem2> ScanListTemp;

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

    @SerializedName("BundleNo")
    public String BundleNo;

    @SerializedName("PackingDivision")
    public int PackingDivision;

    @SerializedName("Weight")
    public double Weight;

    @SerializedName("PalletType")
    public String PalletType;

    @SerializedName("Bin")
    public String Bin;

    @SerializedName("LocationName")
    public String LocationName;

    @SerializedName("SearchDivision")
    public int SearchDivision;

    @SerializedName("ContainerNo")
    public String ContainerNo;

    @SerializedName("OutDate")
    public String OutDate;

    @SerializedName("SilNo")
    public String SilNo;

    @SerializedName("ContainerTon")
    public double ContainerTon;

    @SerializedName("CarNumber")
    public String CarNumber;

    @SerializedName("ContainerNo2")
    public String ContainerNo2;

    @SerializedName("Image")
    public String Image;

    @SerializedName("ImageName")
    public String ImageName;

    @SerializedName("StockOutNo")
    public String StockOutNo;

    @SerializedName("StockOutType")
    public int StockOutType;

    @SerializedName("GPackingNo")
    public String GPackingNo;

    @SerializedName("GLocationNo")
    public int GLocationNo;

    @SerializedName("ALQty")
    public double ALQty;

    @SerializedName("ACQty")
    public double ACQty;

    @SerializedName("STQty")
    public double STQty;

    @SerializedName("StockInFlag")
    public int StockInFlag;

    @SerializedName("AreaCode")
    public String AreaCode;

    @SerializedName("TransportDivision")
    public int TransportDivision;

    @SerializedName("CarTon")
    public double CarTon;

    @SerializedName("TransportAmt")
    public double TransportAmt;

    @SerializedName("Remark1")
    public String Remark1;

    @SerializedName("Remark2")
    public String Remark2;

    @SerializedName("CustomerClass")
    public int CustomerClass;

    @SerializedName("ItemType")
    public int ItemType;

    @SerializedName("BoolRadio1")
    public boolean BoolRadio1;

    @SerializedName("BoolRadio2")
    public boolean BoolRadio2;

    @SerializedName("NumPrint")
    public int NumPrint;

    @SerializedName("BReturn")
    public boolean BReturn;

    @SerializedName("StockOrderLocationNo")
    public int StockOrderLocationNo;

    @SerializedName("SaleOrderLocationNo")
    public int SaleOrderLocationNo;

    @SerializedName("StockOutLocationNo")
    public int StockOutLocationNo;

    @SerializedName("Action")
    public int Action;

    @SerializedName("WorkDivision")
    public int WorkDivision;

    @SerializedName("ItemTag")
    public String ItemTag;

    @SerializedName("ServiceType")
    public int ServiceType;

    public SearchCondition(){}

}
