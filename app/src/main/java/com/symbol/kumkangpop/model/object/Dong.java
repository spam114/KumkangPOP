package com.symbol.kumkangpop.model.object;
import com.google.gson.annotations.SerializedName;

public class Dong {

    @SerializedName("ErrorCheck")
    public String ErrorCheck="";
    @SerializedName("Dong")
    public String Dong="";
    @SerializedName("ProgressYearMonth")
    public String ProgressYearMonth="";
    @SerializedName("ProgressDate")
    public String ProgressDate="";
    @SerializedName("ExProgressYearMonth")
    public String ExProgressYearMonth="";
    @SerializedName("ExProgressDate")
    public String ExProgressDate="";
    @SerializedName("ProgressFloor")
    public String ProgressFloor="";
    @SerializedName("ExProgressFloor")
    public String ExProgressFloor="";
    @SerializedName("ConstructionEmployee")
    public String ConstructionEmployee="";
    @SerializedName("CollectEmployee")
    public String CollectEmployee="";
    @SerializedName("Type")
    public String Type="";

    //searchCondition
    @SerializedName("ContractNo")
    public String ContractNo="";
    @SerializedName("FromDate")
    public String FromDate="";


    /*public String getErrorCheck() {
        return ErrorCheck;
    }
    public void setErrorCheck(String ErrorCheck) {
        this.ErrorCheck = ErrorCheck;
    }
    public String getDong() {
        return Dong;
    }
    public void setDong(String Dong) {
        this.Dong = Dong;
    }
    public String getProgressYearMonth() {
        return ProgressYearMonth;
    }
    public void setProgressYearMonth(String ProgressYearMonth) {
        this.ProgressYearMonth = ProgressYearMonth;
    }
    public String getProgressDate() {
        return ProgressDate;
    }
    public void setProgressDate(String ProgressDate) {
        this.ProgressDate = ProgressDate;
    }
    public String getExProgressYearMonth() {
        return ExProgressYearMonth;
    }
    public void setExProgressYearMonth(String ExProgressYearMonth) {
        this.ExProgressYearMonth = ExProgressYearMonth;
    }
    public String getExProgressDate() {
        return ExProgressDate;
    }
    public void setExProgressDate(String ExProgressDate) {
        this.ExProgressDate = ExProgressDate;
    }
    public String getProgressFloor() {
        return ProgressFloor;
    }
    public void setProgressFloor(String ProgressFloor) {
        this.ProgressFloor = ProgressFloor;
    }
    public String getExProgressFloor() {
        return ExProgressFloor;
    }
    public void setExProgressFloor(String ExProgressFloor) {
        this.ExProgressFloor = ExProgressFloor;
    }
    public String getConstructionEmployee() {
        return ConstructionEmployee;
    }
    public void setConstructionEmployee(String ConstructionEmployee) {
        this.ConstructionEmployee = ConstructionEmployee;
    }
    public String getCollectEmployee() {
        return CollectEmployee;
    }
    public void setCollectEmployee(String CollectEmployee) {
        this.CollectEmployee = CollectEmployee;
    }
    public String getType() {
        return Type;
    }
    public void setType(String Type) {
        this.Type = Type;
    }

    public void setSearchCondition(String ContractNo, String FromDate){
       this.ContractNo=ContractNo;
       this.FromDate=FromDate;
    }*/

    /*public Dong() {
        super();

    }

    public Dong(String Dong, String ProgressYearMonth, String ProgressFloor, String ConstructionEmployee) {
        super();

        this.Dong=Dong;
        this.ProgressYearMonth=ProgressYearMonth;
        this.ProgressFloor=ProgressFloor;
        this.ConstructionEmployee=ConstructionEmployee;
    }

    public Dong(String Dong, String ConstructionEmployee) {
        super();

        this.Dong=Dong;
        this.ConstructionEmployee=ConstructionEmployee;
    }*/

}
